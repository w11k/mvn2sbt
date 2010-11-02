package com.weiglewilczek.mvn2sbt.specs

import com.weiglewilczek.mvn2sbt._
import org.scalatest._
import matchers.ShouldMatchers
import java.io._

class Mvn2SbtTest extends FlatSpec with ShouldMatchers {

  "Mvn2Sbt" should "throw a FileNotFoundException when pom.xml is not found" in {
    evaluating { ProjectPomReader.read("wrongpom.xml") } should produce [FileNotFoundException]
  }
}
