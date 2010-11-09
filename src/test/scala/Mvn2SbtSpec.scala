package com.weiglewilczek.mvn2sbt.specs

import com.weiglewilczek.mvn2sbt._
import java.io._
import org.specs.mock.Mockito
import org.specs.Specification
import org.apache.ivy.core.module.descriptor.{ModuleDescriptor => MD}

class Mvn2SbtSpec extends Specification with Mockito {

  "Calling ProjectPomReader.read" should {

    "throw a FileNotFoundException when pom.xml does not exist." in {
      ProjectPomReader.read("wrongpom.xml") must throwA[FileNotFoundException]
    }
  }

  "Calling ProjectFileWriter with NULL" should {
    val PFW = mock[ProjectFileWriter]

    "not call writeDefaultWebProject." in {
      PFW(EMPTYFILE, NULLMD)
      PFW.writeDefaultWebProject(EMPTYFILE, NULLMD) wasnt called
    }

    "not call writeDefaultProject." in {
      PFW(EMPTYFILE, NULLMD)
      PFW.writeDefaultProject(EMPTYFILE, NULLMD) wasnt called
    }
  }

  private val EMPTYFILE = new File("")
  private val NULLMD: MD = null
}
