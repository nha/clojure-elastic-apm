(ns clojure-elastic-apm.ring
  (:require [clojure-elastic-apm.core :refer [wrap-apm-transaction]]))

(defn wrap-apm-transaction [handler]
  (fn [{:keys [request-method uri] :as request}]
    (let [tx-name (str (.toUpperCase (name request-method)) " " uri)]
      (with-apm-transaction [tx {:name tx-name :type Transaction/TYPE_REQUEST}]
        (let [{:keys [status] :as response} (handler (assoc request :clojure-elastic-apm/transaction tx))]
          (when status
            (.setResult tx (str "HTTP " status)))
          response)))))
