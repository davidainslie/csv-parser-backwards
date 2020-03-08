package com.backwards.csv

import cats.implicits._
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CsvParserSpec extends AnyWordSpec with Matchers {
  "CSV parser using default configurations" should {
    val csvParser = new CsvParser(CsvConfig())

    "parse a simple line" in {
      val line =
        """aa,bb""".stripMargin

      csvParser.parse(line) mustBe List(List("aa", "bb")).asRight
    }

    "parse a simple line that ends with a new line" in {
      val line =
        """aa,bb
          |""".stripMargin

      csvParser.parse(line) mustBe List(List("aa", "bb")).asRight
    }

    "parse a line that spans multiple lines" in {
      val line =
        """a,"a split
          |cell",
          |b,"something else"""".stripMargin

      csvParser.parse(line) mustBe List(List("a", "a split\ncell", ""), List("b", "something else")).asRight
    }

    "fail to parse incomplete line" in {
      csvParser.parse("\"") mustBe "end of input expected".asLeft
    }
  }

  "CSV parser using configuration overrides" should {
    val csvConfig = CsvConfig(quote = Quote("'"), fieldDelimiter = FieldDelimiter("::"))
    val csvParser = new CsvParser(csvConfig)

    "parse a simple line" in {
      val line =
        """cc::dd""".stripMargin

      csvParser.parse(line) mustBe List(List("cc", "dd")).asRight
    }
  }
}