(ns url-shortener.core-test
  (:require [clojure.test :refer :all]
            [url-shortener.core :as sut]))

(deftest test-get-idx
  (testing "legal args" 
    (are [a b] (= b (#'sut/get-idx a))
      8448 136.0 4944 79.0 5310 85.0 9626 155.0 5217 84.0))
  (testing "illegal args"
    (is (thrown? ClassCastException (#'sut/get-idx "8448")))
    (is (thrown? ClassCastException (#'sut/get-idx [8448])))
    (is (thrown? ClassCastException (#'sut/get-idx {:i 8448})))))

(deftest test-get-character-by-idx
  (testing "legal args"
    (are [a b] (= b (#'sut/get-character-by-idx a))
      5047 \P 7828 \G 1313 \B 9172 \w 592 \Y))
  (testing "illegal args"
    (is (thrown? ClassCastException (#'sut/get-character-by-idx "5047")))
    (is (thrown? ClassCastException (#'sut/get-character-by-idx [5047])))
    (is (thrown? ClassCastException (#'sut/get-character-by-idx {:i 5047})))
    (is (nil? (#'sut/get-character-by-idx -5047)))))

(deftest test-int->id
  (testing "legal args"
    (are [a b] (= b (sut/int->id a))
      5654444 "Niyi" 3233147 "DZ5X" 3592358 "F4XG" 2786217 "Bgoz" 9886206 "fTqw"))
  (testing "illegal args"
    (is (thrown? ClassCastException (sut/int->id "5654444")))
    (is (thrown? ClassCastException (sut/int->id [5654444])))
    (is (thrown? ClassCastException (sut/int->id {:int 56544447})))
    (is (empty? (sut/int->id -56544447)))))

(deftest test-id->int
  (testing "legal args"
    (are [a b] (= b (sut/id->int a))
      "JyX" 76789 "5WQ7" 1316267 "0ztUE" 14751302 "nu95N1" 45720149183))
  (testing "illegal args"
    (is (thrown? IllegalArgumentException (sut/id->int 76789)))
    (is (thrown? NullPointerException (sut/id->int ["JyX"])))
    (is (thrown? ClassCastException (sut/id->int {:id "JyX"})))))



(comment (run-tests))
