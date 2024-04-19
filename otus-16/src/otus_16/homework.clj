(ns otus-16.homework
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn parse-line [line]
  (let [reg #".+\] \".*?/(.*?) .*?\" \d+ (\d+) \"(.*?)\".+"
        [_ url bytes referer] (re-matches reg line)]
    {:url url 
     :bytes (if (str/blank? bytes) 0 (Integer/parseInt bytes)) 
     :referer referer}))

; при вводе без ключей и с ключом url-requested читаем sum-bytes, c ключом referer-requested - count-url
(defn read-log [log-file & {:keys [url-requested referer-requested]}]
  (with-open [rdr (io/reader log-file)]
    (loop [rem-log (line-seq rdr)
           acc {:count-url 0 :sum-bytes 0}]
      (if-not (seq rem-log) acc
              (recur (drop 2 rem-log)
                     (let [{:keys [url bytes referer]} (parse-line (first rem-log))] 
                       (if (or (= url url-requested) 
                               (= referer referer-requested) 
                               (every? nil? [url-requested referer-requested])) 
                         (-> acc 
                             (update :count-url inc)
                             (update :sum-bytes #(+ % bytes)))
                         acc)))))))

(defn read-logs [log-files & {:keys [url-requested referer-requested]}]
  (->> (pmap #(read-log % :url-requested url-requested
                        :referer-requested referer-requested) log-files)
       (apply merge-with +)))

(comment
(-> (map (partial str "resources/access.log.") (range 2 10))
    read-logs))
