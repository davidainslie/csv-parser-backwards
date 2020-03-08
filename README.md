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

When **running** provide the **csv** file and any optional arguments as follows:

- **--csv** or **-c** the path (relative/absolute) of the csv file e.g.

  --csv=src/main/resources/my-input.csv

- **--header** or **-h** optional true/false indicating whether the csv includes a header (default is **false**) e.g.

  --header=true
  
- **--quote** or **-q** optional quote allowing for a line to span multiple lines (default is **""**) e.g.

  --quote='

- **--fieldDelimiter** or **-f** optional marker between fields (default is **,**) e.g.

  --fieldDelimiter=;

- **--lineDelimiter** or **-l** optional marker between lines (default is **\n**) e.g.

  --lineDelimiter=\r\n

Following are some example [csv files](src/main/resources) which can be used to run (showing use of overriding command arguments). However, note that often the value of an argument will have to be quoted e.g. if we chose **;** (semicolon) to be a field delimiter, this would have to be quoted as **";"** because semicolon on the command line is also a way to separate commands. Unfortunately, sbt is not very good at quoting, so we either have to jump into the sbt shell before running the application, or package up the application and then run it. Let's go through some of the various ways.

A CSV that has a header and defaults all other configurations:

```bash
sbt 'run --csv=src/main/resources/includes-header.csv --header=true'
```

A CSV without a header and overrides field delimiter:

```bash
sbt 'run -c=src/main/resources/excludes-header.csv --fieldDelimiter=";"'
```

Note that the **field delimiter** must be **quoted**.

But what if we wanted to override the default **"** double quote to be **'** single quote? The following would not work:

```bash
sbt 'run -c=src/main/resources/excludes-header-single-quote.csv --quote="'" --fieldDelimiter=";"' 
```

the single quote defined by **--quote** effectively closes the first single quote at the start of the sbt command. We can get around this by first stepping into the sbt shell and then running the command:

```bash
sbt
```

and then:

```scala
run -c=src/main/resources/excludes-header-single-quote.csv --quote="'" --fieldDelimiter=";"
```

Or, we can **package** the application providing **univeral** scripts to run, allowing us to provide the command arguments.

From the **project root**, first build the application:

```bash
sbt universal:packageBin
```

and then:

```bash
target/universal/stage/bin/csv-parser-backwards -c=src/main/resources/excludes-header-single-quote.csv --quote="'" --fieldDelimiter=";"
```

(For a Windows machine the **csv-parser-backwards** would be replaced by **csv-parser-backwards.bat**).

Finally, here is an example running against a large CSV:

```bash
sbt 'run --csv=src/main/resources/500000-sales-records.csv --header=true'
```

![App resources](docs/images/app-resources.png)