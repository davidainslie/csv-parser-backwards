package com.backwards.csv

import java.io.File
import java.nio.file.Paths
import cats.effect.ExitCode
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.tag._

class AppSpec extends AnyWordSpec with Matchers {
  "App" should {
    "parse a CSV of 2 lines with a header" in {
      val file = new File("src/main/resources/includes-header.csv")
      val csvParser = new CsvParser(CsvConfig(header = true.tag[Header]))

      val app = App.parse(Paths.get(file.toURI), csvParser)

      app.compile.lastOrError.unsafeRunSync mustBe 2
    }

    "parse a CSV of 3 lines without a header" in {
      val file = new File("src/main/resources/excludes-header.csv")
      val csvParser = new CsvParser(CsvConfig(fieldDelimiter = ";".tag[FieldDelimiter]))

      val app = App.parse(Paths.get(file.toURI), csvParser)

      app.compile.lastOrError.unsafeRunSync mustBe 3
    }

    "highlight invalid lines when parsing a CSV not matching command line arguments" in {
      val file = new File("src/main/resources/excludes-header.csv")
      val csvParser = new CsvParser(CsvConfig())

      val app = App.parse(Paths.get(file.toURI), csvParser)

      app.compile.lastOrError.unsafeRunSync mustBe 0
    }

    "run" in {
      val args = List("--csv=src/main/resources/includes-header.csv", "--header=true")

      App.run(args).unsafeRunSync mustBe ExitCode.Success
    }

    "fail to run because of invalid command arguments" in {
      App.run(Nil).unsafeRunSync mustBe ExitCode.Error
    }
  }
}