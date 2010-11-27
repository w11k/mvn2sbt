package com.weiglewilczek.mvn2sbt

import freemarker.template._
import java.io.File

trait TemplateWriter {

  val config = new Configuration
  config.setDirectoryForTemplateLoading(new File(System.getProperty("java.io.tmpdir")))
  config.setObjectWrapper(new DefaultObjectWrapper())

}
