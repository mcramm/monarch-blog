# Monarch Blog

Example app using [monarch](https://github.com/mcramm/monarch).

Clone this repo down and do the following:

```bash
$ lein deps
$ createdb monarch_blog
$ export DATABASE_URL="postgresql://localhost:5432/monarch_blog"
$ lein monarch :setup
$ lein monarch :up
```

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.
You will need [postgres][2] 9.3.4 installed (recommended)

[1]: https://github.com/technomancy/leiningen
[2]: http://www.postgresql.org

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2014 G. Michael Cramm
