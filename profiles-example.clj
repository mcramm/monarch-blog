; An example of what your project-specific configs might look like in
; ~/.lein/profiles.clj
{:user
 {:env
  {:monarch-blog {:database-url "postgresql://localhost:5432/monarch_blog"}
   :helloworld {:database-url "postgresql://localhost:5432/helloworld"}}}}
