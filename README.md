# CSV Parser by Backwards

Scala implementation of CSV parser

## Prerequisites

I'm sure the local environment is set up for Scala/Java, but just in case, for any Mac user (apologies to everyone else):

If you don't have [Homebrew](https://brew.sh/):

```bash
$ /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

And if you don't have [Java](https://www.java.com/) and/or [SBT](https://www.scala-sbt.org/):

```bash
$ brew install homebrew/cask/java

$ brew install sbt
```

## SBT

From root of this project...

#### Test

```bash
$ sbt clean coverage test
```

#### Generate coverage reports

```bash
$ sbt coverageReport
```

#### Run

When **running** provide the **csv** file and if necessary does it include a **header** - by default a header is not expected. The options to use when running are:

- **--csv** or **-c** providing the path (relative/absolute) of the csv file e.g.

  --csv=src/main/resources/my-input.csv

- **--hasHeader** or **-h** providing true/false indicating whether the csv includes a header (the default is **false**) e.g.

  --hadHeader=true

There are some example [csv files](src/main/resources) which can be used to run e.g.

```bash
sbt "run --csv=src/main/resources/includes-header.csv --hasHeader=true"
```

```bash
sbt "run --csv=src/main/resources/excludes-header.csv"
```

There are also the following configurations with defaults in [application.conf](src/main/resources/application.conf):

- **quote**: csv lines can span multiple lines via quoting, where **"** is the default
- **field-delimiter**: separate fields within a line, where **,** is the default
- **line-delimiter**: designate end of line, where **\n** is the default

Using sbt to override via system properties is a tad convoluted. Instead (though also slightly convoluted) an overriding application configuration can be specified such as [application-overrides.conf](src/main/resources/application-overrides.conf) which has the following (where property **line-delimiter** is not declared so will use the default): 

```yaml
csv {
  field-delimiter = ";"
}
```

and to use the (custom) application configuration:

```bash
sbt '; set javaOptions += "-Dconfig.resource=application-overrides.conf"; run --csv=src/main/resources/excludes-header.csv'
```

Finally, here is an example running against a large CSV:

```bash
sbt "run --csv=src/main/resources/<large?>.csv --hasHeader=true"
```