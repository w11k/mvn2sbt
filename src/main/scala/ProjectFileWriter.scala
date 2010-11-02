package com.weiglewilczek.mvn2sbt

import java.io.{FileWriter, File}
import org.apache.ivy.core.module.descriptor.{ModuleDescriptor => MD}

object ProjectFileWriter {

  /**
   * Checking which kind of project is to be converted.
   */
  def write(output: File, md: MD) = md match {
    case empty: MD if(md.getAllArtifacts.isEmpty) => println("Empty project?")
    case war: MD if(md.getAllArtifacts.first.getType == "war") => writeDefaultWebProject(output, md)
    case jar: MD if(md.getAllArtifacts.first.getType == "jar") => writeDefaultProject(output, md)
    case _ => 
  }

  /**
   * Writing out a Default Project.
   */
  def writeDefaultProject(output: File, md: MD) {
    if (output.getParentFile != null) output.getParentFile.mkdirs
    val fw = new FileWriter(output)
    val dependencies = md.getDependencies

    fw.write("import sbt._\n")
    fw.write("\n")
    fw.write("class Project(info: ProjectInfo) extends DefaultProject(info) {")
    fw.write("\n\n")
    dependencies.foreach { d =>
      fw.write("  val " + d.getDependencyRevisionId.getName.replace("-","_") + " = ")
      fw.write("\""+d.getDependencyRevisionId.getOrganisation+"\"" + " % ")
      fw.write("\""+d.getDependencyRevisionId.getName+"\"" + " % ")
      fw.write("\""+d.getDependencyRevisionId.getRevision+"\"")
      fw.write("\n")
    }
    fw.write("}")
    fw.write("\n")
    fw.close
  }

  /**
   * Writing out a DefaultWebProject.
   */
  def writeDefaultWebProject(output: File, md: MD) {
    if (output.getParentFile != null) output.getParentFile.mkdirs
    val fw = new FileWriter(output)
    val dependencies = md.getDependencies

    fw.write("import sbt._\n")
    fw.write("\n")
    fw.write("class Project(info: ProjectInfo) extends DefaultWebProject(info) {")
    fw.write("\n\n")
    fw.write("  override def libraryDependencies = Set(\n")
    dependencies.foreach { d =>
      fw.write("    \""+d.getDependencyRevisionId.getOrganisation+"\"" + " % ")
      fw.write("\""+d.getDependencyRevisionId.getName+"\"" + " % ")
      if (d == dependencies.last) fw.write("\""+d.getDependencyRevisionId.getRevision+"\"")
      else fw.write("\""+d.getDependencyRevisionId.getRevision+"\",")
      fw.write("\n")
    }
    fw.write("  ) ++ super.libraryDependencies\n")

    fw.write("}")
    fw.write("\n")
    fw.close
  }
  
}
