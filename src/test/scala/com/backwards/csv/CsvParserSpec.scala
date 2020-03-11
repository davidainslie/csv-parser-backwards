package com.backwards.csv

import cats.implicits._
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.tag._

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

    "parse a line where a field has both quoted and unquoted text" in {
      val line = """"abc,"onetwo,three,doremi"""

      csvParser.parse(line) mustBe List(List("abc,onetwo", "three", "doremi")).asRight
    }

    "fail to parse incomplete line" in {
      csvParser.parse("\"") mustBe "end of input expected".asLeft
    }
  }

  "CSV parser using configuration overrides" should {
    "parse a simple line overriding field delimiter" in {
      val csvConfig = CsvConfig(fieldDelimiter = "::".tag[FieldDelimiter])
      val csvParser = new CsvParser(csvConfig)

      val line =
        """cc::dd""".stripMargin

      csvParser.parse(line) mustBe List(List("cc", "dd")).asRight
    }

    "parse a line that spans multiple lines overriding quote" in {
      val csvConfig = CsvConfig(quote = "'".tag[Quote])
      val csvParser = new CsvParser(csvConfig)

      val line =
        """a,'a split
          |cell',
          |b,'something else'""".stripMargin

      csvParser.parse(line) mustBe List(List("a", "a split\ncell", ""), List("b", "something else")).asRight
    }
  }
}