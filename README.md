# datacenter

**この repo は datacenter ではない。datacenter 運用の「計画器」である。**
名前が機能を示さないので冒頭で名乗る（CLAUDE.md「名前が機能を示さない repo は
README 冒頭で名乗る」）。中身は純粋な `.cljc` が 1 本
（`src/datacenter/murakumo.cljc`）で、facility 単位の運用要求を受けて
**「この操作を実行してよいか」を判定し、実行してよい場合の効果を記述して返す**。
効果を実行する経路はこの repo に無い（下記「現在地」）。

対象業務は施設スコープの運用 —— 障害対応、計画メンテナンス、物理入館審査、
容量（ラック / 電力 / 冷却）予約、変更後のヘルスチェック、入館 PII の破棄。
BPMN-as-actor（ADR-0055）の cljc 境界として `actor-manifest.jsonld` から
移行された scaffold。

**手を動かす手順は [`docs/operator-quickstart.md`](docs/operator-quickstart.md)。**
下の「現在地」を読まずにデプロイを計画しないこと —— 上の段落は設計であって、
稼働状態ではない。

## 何を判定するか

`cell-plan` は 1 セル（= 1 運用操作）につき次を返す:

| 状態 | 条件 | `:effects` |
|---|---|---|
| `:blocked` | 必要な attestation が 1 つでも欠けている | `[]`（空） |
| `:ready` | 7 つの gate がすべて attested | 宣言された collection ごとに 1 件 |

**拒否は黙って起きない** —— `:missing-gates` に欠けた gate だけが名前で並ぶ。
7 つ全部を落とせば 7 つ、1 つだけ落とせばその 1 つが返る（quickstart §3 で
両方向を実演する）。全 20 セルが同じ 7 gate を要求する:

    :council-charter-attestation      :no-platform-held-key-baseline
    :no-probing-baseline              :murakumo-only-inference-baseline
    :did-primary-baseline             :append-only-gate-baseline
    :kotoba-only-substrate-baseline

## 構成

| path | 何か |
|---|---|
| `src/datacenter/murakumo.cljc` | 唯一の実装。純粋関数のみ（I/O 無し）。`cell-specs`（20 セル宣言）/ `missing-gates` / `cell-plan` / `all-cell-plans` |
| `test/datacenter/murakumo_test.cljc` | 契約テスト 9 本 / 278 assertion。セル名をハードコードせず `cell-specs` を introspect するので、宣言が増えても追随する |
| `actor-manifest.jsonld` | actor の宣言（DID・capability・governance rule・sub-actor） |
| `.well-known/did.json` | 公開 DID document |
| `deps.edn` | `:test`（cognitect test-runner）/ `:lint`（clj-kondo） |
| `NOTICE` | Apache-2.0 + etzhayyim Charter Compliance Rider v3.1 |

## 現在地（2026-09-05 実測）

**これは scaffold であって、稼働している actor ではない。** 生成される record は
自分でそう申告する —— `:scaffold true` / `:constitutionalStatus "attested-plan"` /
`:actorBoundary "cljc-migration-scaffold"`。

測って確かめた未接続点を、設計として書かずにそのまま挙げる:

- **効果は実行されない。** `cell-plan` が返す `{:op :mst/put-record …}` は
  *記述*であって呼び出しではない。この repo で `:mst/put-record` が現れるのは
  それを構築する 1 箇所（`murakumo.cljc:186`）だけで、消費側は存在しない。
  誰かがこの plan を受け取って実行する必要がある。
- **`actor-manifest.jsonld` の governance rule 3 本は、このコードでは強制されない。**
  `RULE-DATACENTER-APPROVAL` / `-HEALTHCHECK` / `-AUDIT` は manifest にのみ在り、
  `src/` `test/` のどこにも現れない（`grep` で 0 件）。`cell-plan` が見るのは
  上記 7 つの baseline gate だけで、「承認なしに実行へ進まない」「閉じる前に
  ヘルスチェックを通す」は**まだどこも検査していない**。
- **runtime と edge はこの repo に無い。** manifest は `k8s-langserver` /
  `sveltekit-proxy` を宣言するが、どちらもここには入っていない。
- **DID が 2 つある。** `actor-manifest.jsonld`（`@id`）と `murakumo.cljc`
  （`actor-did`）は `did:web:infra.etzhayyim.com:datacenter` で一致するが、
  `.well-known/did.json` の `id` は `did:web:etzhayyim.com:actor:datacenter`。
  **どちらが正かはこの repo からは決まらない。** 発行する record の `:actorDid`
  には前者が入るので、did.json を信じる検証者とは噛み合わない。
- **`phase` は全セル `:event`。** 段階の区別はまだ入っていない。

## ライセンス

Apache License 2.0 + etzhayyim Charter Compliance Rider v3.1（`NOTICE` を参照）。
