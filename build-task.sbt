import scala.sys.process._

val build = taskKey[Unit]("build")

build :=
  "sbt test".#&&(
    "sbt universal:packageBin"
  ).#&&(
    "cp -R target/universal/stage/bin bin"
  ).#&&(
    "cp -R target/universal/stage/lib lib"
  ).!