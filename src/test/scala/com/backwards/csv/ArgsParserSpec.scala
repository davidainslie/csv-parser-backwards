package com.backwards.csv

import java.io.File
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.csv.ArgsParser._

class ArgsParserSpec extends AnyWordSpec with Matchers {
  "Command line arguments" should {
    "be parsed when providing all options longhand" in {
      val csv = new File("my-input.csv")
      val args = List(s"--csv=${csv.getName}", "--hasHeader=true")

      parse(args) mustBe Some(Args(csv, hasHeader = true))
    }

    "be parsed when providing all options shorthand" in {
      val csv = new File("my-input.csv")
      val args = List(s"-c=${csv.getName}", "-h=true")

      parse(args) mustBe Some(Args(csv, hasHeader = true))
    }

    "be parsed when providing all mandatory options" in {
      val csv = new File("my-input.csv")
      val args = List(s"--csv=${csv.getName}")

      parse(args) mustBe Some(Args(csv))
    }

    "not be parsed when missing mandatory options" in {
      parse(Nil) mustBe None
    }
  }
}