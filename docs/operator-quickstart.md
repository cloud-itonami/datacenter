# Operator quickstart

この repo で今日できることを、実際にやる順で並べる。**下のコマンドは全て
2026-09-05 にこの tree に対して実行したもので、示した出力はそれが実際に出した
出力である。** 繋がっていないものは「繋がっていない」と書く（設計を動作のように
書かない）—— デプロイを計画する前に §5「まだ繋がっていないもの」を読むこと。

関連: `README.md`（これが何か）、`actor-manifest.jsonld`（actor の宣言）。

## 0. 前提

JVM と Clojure CLI だけ。ネットワークに出るのは初回の依存解決のみで、
このページのどのコマンドも外部サービスを呼ばない。認証情報は要らない。

    clojure --version   # Clojure CLI version 1.12.5.1654
    java -version       # openjdk version "24.0.2" (Temurin-24.0.2+12)

これはこのページを歩いたときの版であって、最低要件ではない。

## 1. テストを走らせる

    clojure -M:test

9 本・278 assertion が緑で、exit 0:

    Running tests in #{"test"}

    Testing datacenter.murakumo-test

    Ran 9 tests containing 278 assertions.
    0 failures, 0 errors.

assertion 数が多いのは、テストがセル名をハードコードせず `cell-specs` を
introspect して 20 セル全部に同じ契約を当てるため。**セルを増やすと assertion が
自動で増える** —— 増えなければ、新しいセルは宣言されていない。

## 2. lint

    clojure -M:lint

    src/datacenter/murakumo.cljc:194:14: warning: unused binding input
    linting took 984ms, errors: 0, warnings: 1

**warning 1 件が既知の現在地**（`cell-plan` の分配束縛 `:as input` が本体で
使われていない）。`--fail-level error` なので exit は 0。この 1 件が 2 件に
なったら、それはあなたの変更である。

## 3. 門が閉まることを自分の目で見る

このコードの仕事は「実行してよいか」の判定なので、**閉まるところと開くところの
両方**を見ないと、判定しているのか素通りしているのか区別できない。

### 3a. attestation 無し → 拒否

    clojure -M -e '
    (require (quote [datacenter.murakumo :as m]))
    (let [p (m/cell-plan :reviewchange {})]
      (println "status      :" (:status p))
      (println "effects     :" (:effects p))
      (doseq [g (:missing-gates p)] (println "  -" g)))'

    status      : :blocked
    effects     : []
      - :council-charter-attestation
      - :no-platform-held-key-baseline
      - :no-probing-baseline
      - :murakumo-only-inference-baseline
      - :did-primary-baseline
      - :append-only-gate-baseline
      - :kotoba-only-substrate-baseline

`:effects` が空であることを確かめる。**`:blocked` と書いてあるのに効果が
入っている、が一番危ない壊れ方**なので、status ではなく効果の数を見る。

### 3b. 7 つ揃える → 通る

    clojure -M -e '
    (require (quote [datacenter.murakumo :as m]) (quote [clojure.pprint :as pp]))
    (def att (into {} (map (fn [g] [g (str "attested-" (name g))])) m/common-gates))
    (let [p (m/cell-plan :reviewchange
                         {:attestations att :request-id "req-1"
                          :computed-at "2026-09-05T12:00:00Z"})]
      (println "status       :" (:status p))
      (println "effect count :" (count (:effects p)))
      (pp/pprint (first (:effects p))))'

    status       : :ready
    effect count : 1
    {:op :mst/put-record,
     :actor "did:web:infra.etzhayyim.com:datacenter",
     :collection "com.etzhayyim.datacenter.reviewchange",
     :rkey "req-1",
     :record
     {:$type "com.etzhayyim.datacenter.reviewchange",
      :actorBoundary "cljc-migration-scaffold",
      :legacyCell "com-etzhayyim-apps-datacenter-reviewChange",
      :phase :event,
      :computedAt "2026-09-05T12:00:00Z",
      :requestId "req-1",
      :constitutionalStatus "attested-plan",
      :actorDid "did:web:infra.etzhayyim.com:datacenter",
      :scaffold true}}

**この effect は記述であって、実行ではない。** 実行する経路はこの repo に
無い（§5）。

### 3c. 1 つだけ落とす → その 1 つが名指しされる

3a と 3b だけでは「何かがあれば通る」しか言えない。gate が**自分が名乗って
いる理由で**拒否していることは、1 つだけ抜いて確かめる:

    clojure -M -e '
    (require (quote [datacenter.murakumo :as m]))
    (def att (into {} (map (fn [g] [g (str "attested-" (name g))])) m/common-gates))
    (let [p (m/cell-plan :reviewchange
                         {:attestations (dissoc att :no-probing-baseline)
                          :request-id "req-1"})]
      (println "status       :" (:status p))
      (println "missing-gates:" (:missing-gates p))
      (println "effects      :" (:effects p)))'

    status       : :blocked
    missing-gates: [:no-probing-baseline]
    effects      : []

抜いた gate だけが返る。**別の gate 名が返る、または 7 つ全部が返るなら、
判定は入力を見ていない。**

## 4. 宣言されているセルを見る

    clojure -M -e '
    (require (quote [datacenter.murakumo :as m]))
    (println "cells:" (count m/cell-specs))
    (doseq [[k s] (sort-by key m/cell-specs)]
      (println (format "  %-24s %s" (name k) (:legacy-cell s))))'

20 セルが出る。`:legacy-cell` は移行元の名前で、`cell-specs` がこの repo に
おける正本。セルを足すときはここに 1 エントリ足す —— テスト（§1）は
introspect するので、契約テストは自動で新しいセルに当たる。

## 5. まだ繋がっていないもの

ここを設計として読まないこと。**測って確かめた不在**である:

- **効果を実行するものが無い。** `:mst/put-record` はこの repo では構築される
  だけ（`src/datacenter/murakumo.cljc:186` の 1 箇所）で、消費側が無い。
  §3b の出力は「こうせよ」という plan であって、何も書き込まれていない。
- **manifest の governance rule 3 本は未実装。**
  `RULE-DATACENTER-APPROVAL` / `-HEALTHCHECK` / `-AUDIT` は
  `actor-manifest.jsonld` にのみ在り、`src/` にも `test/` にも現れない
  （`grep -rn RULE-DATACENTER src/ test/` が 0 件）。**「承認なしに実行へ
  進まない」は、今どこも検査していない。**
- **runtime / edge は別の場所。** manifest が宣言する `k8s-langserver` と
  `sveltekit-proxy` はこの repo に入っていない。
- **DID が 2 つある。** `.well-known/did.json` は
  `did:web:etzhayyim.com:actor:datacenter`、manifest と実装は
  `did:web:infra.etzhayyim.com:datacenter`。発行される record には後者が入る。
  どちらが正かはこの repo からは決まらないので、検証側を繋ぐ前に確認すること。

## 6. 変更を入れるとき

1. `clojure -M:test` を**変更前に**通す（緑であることを確認してから触る。
   変更後に赤くなったとき、それが自分のものだと言えるようにするため）。
2. 変更する。
3. `clojure -M:test` と `clojure -M:lint` を通す。warning は 1 件のまま
   （§2）であることを確認する。
4. gate の挙動を変えたなら、§3a / §3b / §3c を**3 つとも**walk し直す。
   閉まる方だけ、開く方だけでは、判定が壊れたことを検出できない。
