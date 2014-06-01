(defproject monarch-blog "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [org.clojure/java.jdbc "0.3.3"]
                 [postgresql "9.3-1101.jdbc4"]]
  :plugins [[lein-ring "0.8.10"]
            [lein-environ "0.5.0"]
            [monarch "0.2.2"]]
  :migrations {:table "schema_versions"
               :location "data/schema_migrations"
               :config-lens :monarch-blog}
  :ring {:handler monarch-blog.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
