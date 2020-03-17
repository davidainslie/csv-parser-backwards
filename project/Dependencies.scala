import sbt._

object Dependencies {
  def apply(): Seq[sbt.ModuleID] = Seq(
    scalatest, scalatestplus, scalacheck, scalacheckShapeless,
    scribe, scopt, pureConfig,
    cats, mouse, simulacrum, refined, monocle, shapeless, kittens,
    scalaParserCombinator, fs2, betterFiles
  ).flatten

  lazy val scalatest: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % "3.1.1" % Test withSources() withJavadoc()
  )

  lazy val scalatestplus: Seq[ModuleID] = Seq(
    "org.scalatestplus" %% "scalatestplus-scalacheck" % "3.1.0.0-RC2" % Test
  )

  lazy val scalacheck: Seq[ModuleID] = Seq(
    "org.scalacheck" %% "scalacheck" % "1.14.3" % Test withSources() withJavadoc()
  )

  lazy val scalacheckShapeless: Seq[ModuleID] = Seq(
    "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.5" % Test
  )

  lazy val scribe: Seq[ModuleID] = Seq(
    "com.outr" %% "scribe" % "2.7.12" withSources() withJavadoc()
  )

  lazy val scopt: Seq[ModuleID] = Seq(
    "com.github.scopt" %% "scopt" % "4.0.0-RC2"
  )

  lazy val pureConfig: Seq[ModuleID] = {
    val group = "com.github.pureconfig"
    val version = "0.12.3"

    Seq("pureconfig").map(group %% _ % version withSources() withJavadoc())
  }

  lazy val cats: Seq[ModuleID] = {
    val group = "org.typelevel"
    val version = "2.1.1"

    Seq(
      "cats-core", "cats-effect"
    ).map(group %% _ % version withSources() withJavadoc()) ++ Seq(
      "cats-laws", "cats-testkit"
    ).map(group %% _ % version % Test withSources() withJavadoc())
  }

  lazy val mouse: Seq[ModuleID] = Seq(
    "org.typelevel" %% "mouse" % "0.24"
  )

  lazy val simulacrum: Seq[ModuleID] = Seq(
    "org.typelevel" %% "simulacrum" % "1.0.0" withSources() withJavadoc()
  )

  lazy val refined: Seq[ModuleID] = {
    val group = "eu.timepit"
    val version = "0.9.13"

    Seq(
      "refined", "refined-cats"
    ).map(group %% _ % version withSources() withJavadoc())
  }

  lazy val monocle: Seq[ModuleID] = {
    val group = "com.github.julien-truffaut"
    val version = "2.0.4"

    Seq(
      "monocle-core", "monocle-macro", "monocle-generic"
    ).map(group %% _ % version withSources() withJavadoc())
  }

  lazy val shapeless: Seq[ModuleID] = Seq(
    "com.chuusai" %% "shapeless" % "2.3.3"
  )

  lazy val kittens: Seq[ModuleID] = Seq(
    "org.typelevel" %% "kittens" % "2.0.0"
  )

  lazy val scalaParserCombinator: Seq[ModuleID] = Seq(
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"
  )

  lazy val fs2: Seq[ModuleID] = {
    val group = "co.fs2"
    val version = "2.2.2"

    Seq(
      "fs2-core", "fs2-io", "fs2-reactive-streams"
    ).map(group %% _ % version withSources() withJavadoc())
  }

  lazy val betterFiles: Seq[ModuleID] = Seq(
    "com.github.pathikrit" %% "better-files" % "3.8.0"
  )
}