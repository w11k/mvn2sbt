import sbt._

class Mvn2SbtProject(info: ProjectInfo) extends ProcessorProject(info) {

  // Dependencies

  val ivy = "org.apache.ivy" % "ivy" % "2.2.0" withSources

  // Testing

  val spec = "org.scala-tools.testing" % "specs" % "1.6.2" % "test"

  val mockito = "org.mockito" % "mockito-all" % "1.8.0" % "test"
}
