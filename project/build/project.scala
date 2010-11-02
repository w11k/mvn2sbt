import sbt._

class Mvn2SbtProject(info: ProjectInfo) extends ProcessorProject(info) {

  val ivy = "org.apache.ivy" % "ivy" % "2.1.0" withSources

  val scalatest = "org.scalatest" % "scalatest" % "1.0" % "test"
}
