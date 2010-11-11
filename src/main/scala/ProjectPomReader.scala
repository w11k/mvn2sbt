package com.weiglewilczek.mvn2sbt

import java.io.File
import org.apache.ivy.{core, plugins}
import core.module.descriptor.ModuleDescriptor
import core.settings.IvySettings
import plugins.parser.m2.PomModuleDescriptorParser

object ProjectPomReader extends ProjectPomReader {

  /**
   * Returns a pom as a ModuleDescriptor.
   * @param pom The pom file which shall be converted.
   */
  def apply(pom: String): ModuleDescriptor = {
    PomModuleDescriptorParser.getInstance.parseDescriptor(new IvySettings, toURL(new java.io.File(pom)), true)
  }
}

trait ProjectPomReader {

  def apply(pom: String): ModuleDescriptor

  private def toURL(file: File) = file.toURI.toURL
}
