(defproject othello "0.1.0-SNAPSHOT"
  :description "othello"
  :url "https://github.com/jgraydus/othello"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.finger-tree "0.0.2"]]
  :main ^:skip-aot othello.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
