package com.backwards.csv

import scala.util.matching.Regex
import scala.util.parsing.combinator._
import cats.implicits._

class CsvParser(val csvConfig: CsvConfig) extends RegexParsers {
  override val skipWhitespace = false

  // override val whiteSpace = """[ \t]""".r

  val quote: String = csvConfig.quote.value

  val delimiter: String = csvConfig.fieldDelimiter.value

  val crlf: Parser[String] = csvConfig.lineDelimiter.value // "\r\n" | "\n"

  val text: Regex = "[^%s%s\r\n]".format(quote, delimiter).r // TODO line delimiter instead of \r\n, but so far does not work

  val unquotedField: Parser[String] = rep(text) ^^ (_.mkString)

  val quotedField: Parser[String] =
    quote ~> rep(text | delimiter | crlf) <~ quote ^^ (_.mkString)

  val field: Parser[String] = quotedField | unquotedField

  val line: Parser[List[String]] = repsep(field, delimiter) <~ crlf

  val csv: Parser[List[List[String]]] = rep(line)

  def parse(line: String): String Either List[List[String]] = {
    if (line.endsWith(System.lineSeparator())) // TODO - Use configured line delimiter
      parseAll(csv, line)
    else
      parseAll(csv, line + System.lineSeparator()) // TODO - Use configured line delimiter
  } match {
    case Success(result, _) => result.asRight[String]
    case Failure(message, _) => message.asLeft[List[List[String]]]
    case Error(message, _) => message.asLeft[List[List[String]]]
  }
}