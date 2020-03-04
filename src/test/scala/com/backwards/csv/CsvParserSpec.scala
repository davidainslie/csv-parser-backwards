package com.backwards.csv

import cats.implicits._
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CsvParserSpec extends AnyWordSpec with Matchers {
  "CSV parser using default configurations" should {
    val csvConfig = CsvConfig().getOrElse(throw new Exception)
    val csvParser = new CsvParser(csvConfig)

    "parse a simple line" in {
      val line =
        """aa,bb""".stripMargin

      csvParser.parse(line) mustBe List(List("aa", "bb")).asRight
    }

    "parse a line that spans multiple lines" in {
      val line =
        """a,"a split
          |cell",
          |b,"something else"""".stripMargin

      csvParser.parse(line) mustBe List(List("a", "a split\ncell", ""), List("b", "something else")).asRight
    }
  }

  "CSV parser using configuration overrides" should {
    val csvConfig = CsvConfig(Quote("'"), FieldDelimiter("::"))
    val csvParser = new CsvParser(csvConfig)

    "parse a simple line" in {
      val line =
        """cc::dd""".stripMargin

      csvParser.parse(line) mustBe List(List("cc", "dd")).asRight
    }
  }
}