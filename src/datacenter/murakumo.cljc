(ns datacenter.murakumo
  "Pure cljc actor boundary generated from manifest migration scaffold."
  (:require [clojure.string :as str]))

(def actor-did
  "did:web:infra.etzhayyim.com:datacenter")

(def common-gates
  [:council-charter-attestation
   :no-platform-held-key-baseline
   :no-probing-baseline
   :murakumo-only-inference-baseline
   :did-primary-baseline
   :append-only-gate-baseline
   :kotoba-only-substrate-baseline])

(defn collection
  [name]
  (str "com.etzhayyim.datacenter." name))

(def cell-specs {
  :shinkaevolution {:legacy-cell "com-etzhayyim-apps-standard-shinkaEvolution"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "shinkaevolution")]
     :required-gates common-gates
     :trigger "manifest cell shinkaevolution"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :shinkaknowledge {:legacy-cell "com-etzhayyim-apps-standard-shinkaKnowledge"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "shinkaknowledge")]
     :required-gates common-gates
     :trigger "manifest cell shinkaknowledge"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :shinka {:legacy-cell "shinka"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "shinka")]
     :required-gates common-gates
     :trigger "manifest cell shinka"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :kyumei {:legacy-cell "kyumei"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "kyumei")]
     :required-gates common-gates
     :trigger "manifest cell kyumei"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :startoperation {:legacy-cell "com-etzhayyim-apps-datacenter-startOperation"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "startoperation")]
     :required-gates common-gates
     :trigger "manifest cell startoperation"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :submitintake {:legacy-cell "com-etzhayyim-apps-datacenter-submitIntake"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "submitintake")]
     :required-gates common-gates
     :trigger "manifest cell submitintake"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :reviewchange {:legacy-cell "com-etzhayyim-apps-datacenter-reviewChange"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "reviewchange")]
     :required-gates common-gates
     :trigger "manifest cell reviewchange"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :reportexecution {:legacy-cell "com-etzhayyim-apps-datacenter-reportExecution"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "reportexecution")]
     :required-gates common-gates
     :trigger "manifest cell reportexecution"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :reporthealthcheck {:legacy-cell "com-etzhayyim-apps-datacenter-reportHealthCheck"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "reporthealthcheck")]
     :required-gates common-gates
     :trigger "manifest cell reporthealthcheck"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :stabilizeincident {:legacy-cell "com-etzhayyim-apps-datacenter-stabilizeIncident"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "stabilizeincident")]
     :required-gates common-gates
     :trigger "manifest cell stabilizeincident"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :requestaccess {:legacy-cell "com-etzhayyim-apps-datacenter-requestAccess"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "requestaccess")]
     :required-gates common-gates
     :trigger "manifest cell requestaccess"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :submitaccessdraft {:legacy-cell "com-etzhayyim-apps-datacenter-submitAccessDraft"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "submitaccessdraft")]
     :required-gates common-gates
     :trigger "manifest cell submitaccessdraft"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :reviewaccess {:legacy-cell "com-etzhayyim-apps-datacenter-reviewAccess"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "reviewaccess")]
     :required-gates common-gates
     :trigger "manifest cell reviewaccess"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :reservecapacity {:legacy-cell "com-etzhayyim-apps-datacenter-reserveCapacity"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "reservecapacity")]
     :required-gates common-gates
     :trigger "manifest cell reservecapacity"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :reviewcapacity {:legacy-cell "com-etzhayyim-apps-datacenter-reviewCapacity"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "reviewcapacity")]
     :required-gates common-gates
     :trigger "manifest cell reviewcapacity"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :purgeaccesspii {:legacy-cell "com-etzhayyim-apps-datacenter-purgeAccessPii"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "purgeaccesspii")]
     :required-gates common-gates
     :trigger "manifest cell purgeaccesspii"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :getoperation {:legacy-cell "com-etzhayyim-apps-datacenter-getOperation"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "getoperation")]
     :required-gates common-gates
     :trigger "manifest cell getoperation"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :listforfacility {:legacy-cell "com-etzhayyim-apps-datacenter-listForFacility"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "listforfacility")]
     :required-gates common-gates
     :trigger "manifest cell listforfacility"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :getmyaccessrequest {:legacy-cell "com-etzhayyim-apps-datacenter-getMyAccessRequest"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "getmyaccessrequest")]
     :required-gates common-gates
     :trigger "manifest cell getmyaccessrequest"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :listaccessforfacility {:legacy-cell "com-etzhayyim-apps-datacenter-listAccessForFacility"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "listaccessforfacility")]
     :required-gates common-gates
     :trigger "manifest cell listaccessforfacility"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
})

(defn safe-rkey
  [s]
  (let [clean (-> (str s)
                  (str/replace #"^did:web:" "")
                  (str/replace #"[^A-Za-z0-9._~-]" "-"))]
    (if (str/blank? clean) "unknown" clean)))

(defn gate-value
  [attestations gate]
  (or (get attestations gate)
      (get attestations (name gate))
      (when (set? attestations) (attestations gate))
      (when (set? attestations) (attestations (name gate)))))

(defn missing-gates
  [spec attestations]
  (->> (:required-gates spec)
       (remove #(boolean (gate-value attestations %)))
       vec))

(defn put-record-effect
  [collection rkey record]
  {:op :mst/put-record
   :actor actor-did
   :collection collection
   :rkey rkey
   :record record})

(defn records-for
  [spec {:keys [records record computed-at request-id]
         :as input}]
  (let [input-records (cond
                        (map? records) records
                        (some? record) {0 record}
                        :else {})
        base {:actorDid actor-did
              :computedAt computed-at
              :legacyCell (:legacy-cell spec)
              :phase (:phase spec)
              :requestId request-id
              :actorBoundary "cljc-migration-scaffold"
              :scaffold true
              :constitutionalStatus "attested-plan"}]
    (map-indexed
     (fn [idx coll]
       (let [record* (merge {:$type coll}
                            base
                            (or (get input-records coll)
                                (get input-records idx)
                                {}))
             rkey (safe-rkey (or (:rkey record*)
                                 (get record* "rkey")
                                 (:tid record*)
                                 request-id
                                 (str (:legacy-cell spec) "-" idx)))]
         {:collection coll
          :record record*
          :rkey rkey}))
     (:collections spec))))

(defn cell-plan
  [cell-key {:keys [attestations] :as input}]
  (let [spec (get cell-specs cell-key)]
    (when-not spec
      (throw (ex-info "unknown cell" {:cell cell-key})))
    (let [missing (missing-gates spec attestations)]
      (merge
       {:cell cell-key
        :legacy-cell (:legacy-cell spec)
        :actor actor-did
        :phase (:phase spec)
        :murakumo-node (:murakumo-node spec)
        :trigger (:trigger spec)
        :ceiling (:ceiling spec)
        :required-gates (:required-gates spec)
        :missing-gates missing}
       (if (seq missing)
         {:status :blocked
          :effects []}
         (let [planned-records (records-for spec input)]
           {:status :ready
            :records (vec planned-records)
            :effects (mapv (fn [{:keys [collection record rkey]}]
                             (put-record-effect collection rkey record))
                           planned-records)}))))))

(defn all-cell-plans
  [input]
  (into {}
        (map (fn [cell-key] [cell-key (cell-plan cell-key input)]))
        (keys cell-specs)))
