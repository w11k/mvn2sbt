package com.weiglewilczek.mvn2sbt

import sbt._
import processor._
import java.io.File

class Mvn2SbtProcessor extends BasicProcessor {

  def apply(project: Project, args: String) {
    project.log.info("Trying to convert " + args + " -> project/build/project.scala")
    ProjectFileWriter(new File("project/build/project.scala"), ProjectPomReader.read(args))
  }
}
