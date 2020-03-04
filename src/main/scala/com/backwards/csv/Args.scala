package com.backwards.csv

import java.io.File
import cats.Show

final case class Args(csv: File = new File("."), hasHeader: Boolean = false)

object Args {
  implicit val argsShow: Show[Args] =
    args => s"Args(csv = ${args.csv.getAbsolutePath}, hasHeader = ${args.hasHeader})"
}