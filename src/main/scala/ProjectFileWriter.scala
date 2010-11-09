package com.weiglewilczek.mvn2sbt

import java.io.{FileWriter, File}
import org.apache.ivy.core.module.descriptor.{ModuleDescriptor => MD}
import sbt.ConsoleLogger
import sbt.Level

object ProjectFileWriter extends ProjectFileWriter {

  /**
   * Checking which kind of project is to be converted.
   * @param output Destination file which will be written.
   * @param md ModuleDescriptor which should have project dependencies.
   */
  def apply(output: File, md: MD) = md match {
    case war if (md.getAllArtifacts.first.getType == "war") => writeDefaultWebProject(output, md)
    case jar if (md.getAllArtifacts.first.getType == "jar") => writeDefaultProject(output, md)
    case _ => log(Level.Error, "Only single jar/war projects are supported yet! Converting Failed!")
  }
}

trait ProjectFileWriter extends ConsoleLogger {
  
  def apply(output: File, md: MD)
  
  /**
   * Writing out a Default Project.
   * @param output Destination file which will be written.
   * @param md ModuleDescriptor which should have project dependencies.
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
      fw.write("  val "+d.getDependencyRevisionId.getName.replace("-","_").replace(".","")+" = ")
      fw.write("\""+d.getDependencyRevisionId.getOrganisation+"\"" + " % ")
      fw.write("\""+d.getDependencyRevisionId.getName+"\"" + " % ")
      fw.write("\""+d.getDependencyRevisionId.getRevision+"\"")
      if (!d.getModuleConfigurations.isEmpty)
        fw.write(" % \""+d.getModuleConfigurations.first+"\"")
      if (!d.getAllDependencyArtifacts.isEmpty && d.getAllDependencyArtifacts.first.getAttribute("classifier") != null)
        fw.write(" classifier "+"\""+d.getAllDependencyArtifacts.first.getAttribute("classifier")+"\"")
      fw.write("\n")
    }
    fw.write("}")
    fw.write("\n")
    fw.close
    log(Level.Info, "Converted Successfully!")
  }

  /**
   * Writing out a DefaultWebProject.
   * @param output Destination file which will be written.
   * @param md ModuleDescriptor which should have project dependencies.
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
      fw.write("\""+d.getDependencyRevisionId.getRevision+"\"")
      if (!d.getModuleConfigurations.isEmpty)
        fw.write(" % \""+d.getModuleConfigurations.first+"\"")
      if (!d.getAllDependencyArtifacts.isEmpty && d.getAllDependencyArtifacts.first.getAttribute("classifier") != null)
        fw.write(" classifier "+"\""+d.getAllDependencyArtifacts.first.getAttribute("classifier")+"\"")
      if(d != dependencies.last)
        fw.write(",")
      fw.write("\n")
    }
    fw.write("  ) ++ super.libraryDependencies\n")
    fw.write("}")
    fw.write("\n")
    fw.close
    log(Level.Info, "Converted Successfully!")
  }
  
}
