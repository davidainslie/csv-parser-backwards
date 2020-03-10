package com.backwards.csv

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
//import java.lang.System.lineSeparator

class LineSpec extends AnyWordSpec with Matchers {
  "Line with default quote" should {
    val csvConfig = CsvConfig()
    //val quote = csvConfig.quote

    val conflate: (Line, String) => Line =
      Line.conflate(csvConfig)

    "be conflated from simple/single line" in {
      val line = s"My line${csvConfig.lineDelimiter.value}"

      conflate(Line(), line) mustBe Line(line = line)
    }

    /*"be conflated from multiple lines" in {
      val line = s"My$lineSeparator ${quote.value}line${quote.value} complete$lineSeparator"

      conflate(Line(), line) mustBe Line(line = line)
    }*/

    /*"be pending conflation" in {
      val line = s"My ${quote.value}line"

      conflate(Line(), line) mustBe Line(accumulatedChars = List(line))
    }*/

    /*"be pending conflation where new line would be embedded in quotes" in {
      val line = s"My ${quote.value}line$lineSeparator"

      conflate(Line(), line) mustBe Line(accumulatedChars = List(line))
    }*/
  }

  /*"Line with overriding quote" should {
    val csvConfig = CsvConfig(quote = Quote("#"))
    val quote = csvConfig.quote

    val conflate: (Line, String) => Line =
      Line.conflate(csvConfig)

    "be conflated from simple/single line" in {
      val line = s"My line$lineSeparator"

      conflate(Line(), line) mustBe Line(line = line)
    }

    "be conflated from multiple lines" in {
      val line = s"My$lineSeparator ${quote.value}line${quote.value} complete$lineSeparator"

      conflate(Line(), line) mustBe Line(line = line)
    }

    "be pending conflation" in {
      val line = s"My ${quote}line"

      conflate(Line(), line) mustBe Line(accumulatedChars = List(line))
    }

    "be pending conflation where new line would be embedded in quotes" in {
      val line = s"My ${quote.value}line$lineSeparator"

      conflate(Line(), line) mustBe Line(accumulatedChars = List(line))
    }
  }*/
}