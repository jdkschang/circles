(defproject circles "0.1.0-SNAPSHOT"
  :description "A simple Clojure project for animated circle visualizations"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [quil "4.3.1563"]]
  :main ^:skip-aot circles.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
