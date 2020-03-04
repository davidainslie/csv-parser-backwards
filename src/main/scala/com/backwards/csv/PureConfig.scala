package com.backwards.csv

import scala.language.experimental.macros
import pureconfig._
import pureconfig.generic.ExportMacros

/**
 * PureConfig already has implicits for auto configuration reading by using the following import:
 * import pureconfig.generic.auto._
 * Unforunately (at the time of writing) IntelliJ removes the import when optimising.
 * By copy and pasting the PureConfig implicits into this trait and then mixing in where necessary we prevent IntelliJ making a mistake.
 */
trait PureConfig {
  implicit def exportReader[A]: Exported[ConfigReader[A]] = macro ExportMacros.exportDerivedReader[A]
  implicit def exportWriter[A]: Exported[ConfigWriter[A]] = macro ExportMacros.exportDerivedWriter[A]
}