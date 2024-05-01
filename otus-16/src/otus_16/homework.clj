(ns otus-16.homework
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def reg #".+\] \".*?/(.*?) .*?\" \d+ (\d+) \"(.*?)\".+")

(defn parse-line [line]
  (let [[_ url bytes referer] (re-matches reg line)]
    {:url url
     :bytes (if (str/blank? bytes) 0 (Integer/parseInt bytes))
     :referer referer}))

; при вводе без ключей и с ключом url-requested читаем sum-bytes, c ключом referer-requested - count-url
(defn read-log [log-file & {:keys [url-requested referer-requested]}]
  (with-open [rdr (io/reader log-file)]
    (->> (take-nth 2 (line-seq rdr))
         (map parse-line)
         (reduce (fn [acc {:keys [url bytes referer]}]
                   (cond-> acc 
                     (or (= url url-requested) 
                         (= referer referer-requested) 
                         (every? nil? [url-requested referer-requested])) 
                     ((fn [x] (-> x 
                                 (update :count-url inc) 
                                 (update :sum-bytes #(+ % bytes)))))))
                 {:count-url 0 :sum-bytes 0}))))

(defn read-logs [log-files & {:keys [url-requested referer-requested]}]
  (->> (pmap #(read-log % :url-requested url-requested
                        :referer-requested referer-requested) log-files)
       (apply merge-with +)))

(comment
(-> (map (partial str "resources/access.log.") (range 2 10))
    read-logs))
