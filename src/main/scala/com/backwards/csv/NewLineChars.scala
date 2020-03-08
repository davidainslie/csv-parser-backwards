package com.backwards.csv

final case class NewLineChars(accumulatedChars: List[String] = Nil, chars: String = "")

object NewLineChars {
  def conflate(lineDelimiter: LineDelimiter)(newLineChars: NewLineChars, char: String): NewLineChars = {
    val accumulatedChars = newLineChars.accumulatedChars :+ char

    if (accumulatedChars.mkString.endsWith(lineDelimiter.value)) {
      NewLineChars(Nil, accumulatedChars.mkString.stripSuffix(lineDelimiter.value) + System.lineSeparator)
    } else {
      NewLineChars(accumulatedChars)
    }
  }
}