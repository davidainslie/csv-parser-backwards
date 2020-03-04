package com.backwards.csv

import org.scalatest.EitherValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CsvConfigSpec extends AnyWordSpec with Matchers with EitherValues {
  "CSV Config" should {
    "be loaded from default resource" in {
      CsvConfig() mustBe a [Right[_, _]]
    }

    "be loaded from a given resource" in {
      val csvConfigString =
        """
          |csv {
          |  quote = "\""
          |
          |  field-delimiter = ","
          |
          |  line-delimiter = "\n"
          |}
          |""".stripMargin

      CsvConfig(csvConfigString) mustBe a [Right[_, _]]
    }

    "be loaded from a given resource defaulting properties no provided" in {
      val csvConfigString =
        """
          |csv {
          |}
          |""".stripMargin

      CsvConfig(csvConfigString) mustBe a [Right[_, _]]
    }

    "fail to load from a given resource because a property is not correctly quoted" in {
      val csvConfigString =
        """
          |csv {
          |  quote = "\""
          |
          |  field-delimiter = ,
          |
          |  line-delimiter = "\n"
          |}
          |""".stripMargin

      CsvConfig(csvConfigString) mustBe a [Left[_, _]]
    }
  }
}