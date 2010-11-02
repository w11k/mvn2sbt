package com.weiglewilczek.mvn2sbt

import java.io.File
import org.apache.ivy.{core, plugins}
import core.module.descriptor.ModuleDescriptor
import core.settings.IvySettings
import plugins.parser.m2.PomModuleDescriptorParser

object ProjectPomReader {

  /**
   * Returns a pom as a ModuleDescriptor.
   */
  def read(pom: String): ModuleDescriptor = {
    PomModuleDescriptorParser.getInstance.parseDescriptor(new IvySettings, toURL(new java.io.File(pom)), true)
  }

  private def toURL(file: File) = file.toURI.toURL
}
