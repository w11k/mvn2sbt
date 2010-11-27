package com.weiglewilczek.mvn2sbt

import Mvn2SbtHelpers._
import org.apache.ivy.core.module.descriptor.{ ModuleDescriptor => MD }
import sbt.ConsoleLogger
import sbt.Level
import freemarker.template.{ SimpleSequence, SimpleHash }
import java.io._

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

trait ProjectFileWriter extends ConsoleLogger with TemplateWriter {

  /**
   * Writing out a Default Project, using singleJar template.
   * @param output Destination file which will be written.
   * @param md ModuleDescriptor which should have project dependencies.
   */
  def writeDefaultProject(output: File, md: MD) {
    if (output.getParentFile != null) output.getParentFile.mkdirs

    val out = new FileWriter(output)
    val tmpFile = File.createTempFile("template", ".ftl~")
    tmpFile.deleteOnExit
    val tmpWriter = new BufferedWriter(new FileWriter(tmpFile))

    val lnr = new LineNumberReader(new InputStreamReader(getClass.getResourceAsStream("/singleJar.ftl")))
    while(lnr.ready) {
      tmpWriter.write(lnr.readLine)
      tmpWriter.newLine
    }
    lnr.close
    tmpWriter.close

    var depList = new SimpleSequence
    val dependencies = md.getDependencies

    dependencies foreach { d =>
      val currentDep = new SimpleHash
      currentDep.put("depName", d.getDependencyRevisionId.getName.replace("-","_").replace(".",""))
      currentDep.put("organisation", "\"" + d.getDependencyRevisionId.getOrganisation + "\"")
      currentDep.put("name", convertTodoublePercent(d.getDependencyRevisionId.getName))
      currentDep.put("revision", "% \"" + d.getDependencyRevisionId.getRevision + "\"")
      if (!d.getModuleConfigurations.isEmpty)
        currentDep.put("moduleConfig", "% \"" + d.getModuleConfigurations.first + "\"")
      else currentDep.put("moduleConfig", "")
      if (!d.getAllDependencyArtifacts.isEmpty && d.getAllDependencyArtifacts.first.getAttribute("classifier") != null)
        currentDep.put("classifier", sbtIfier(d.getAllDependencyArtifacts.first.getAttribute("classifier")))
      else currentDep.put("classifier", "")
      depList.add(currentDep)
    }

    val root = new SimpleHash
    root.put("dependencies", depList)

    val templ = config.getTemplate("/" + tmpFile.getName)
    templ.process(root, out)

    out.close
    log(Level.Info, "Converted Successfully!")
  }

  /**
   * Writing out a DefaultWebProject, using singleWar template.
   * @param output Destination file which will be written.
   * @param md ModuleDescriptor which should have project dependencies.
   */
  def writeDefaultWebProject(output: File, md: MD) {
    if (output.getParentFile != null) output.getParentFile.mkdirs

    val out = new FileWriter(output)
    val tmpFile = File.createTempFile("template", ".ftl~")
    tmpFile.deleteOnExit
    val tmpWriter = new BufferedWriter(new FileWriter(tmpFile))

    val lnr = new LineNumberReader(new InputStreamReader(getClass.getResourceAsStream("/singleWar.ftl")))
    while(lnr.ready) {
      tmpWriter.write(lnr.readLine)
      tmpWriter.newLine
    }
    lnr.close
    tmpWriter.close

    var depList = new SimpleSequence
    val dependencies = md.getDependencies

    dependencies foreach { d =>
      val currentDep = new SimpleHash
      currentDep.put("organisation", "\"" + d.getDependencyRevisionId.getOrganisation + "\"")
      currentDep.put("name", convertTodoublePercent(d.getDependencyRevisionId.getName))
      currentDep.put("revision", "% \"" + d.getDependencyRevisionId.getRevision + "\"")
      if (!d.getModuleConfigurations.isEmpty)
        currentDep.put("moduleConfig", "% \"" + d.getModuleConfigurations.first + "\"")
      else currentDep.put("moduleConfig", "")
      if (!d.getAllDependencyArtifacts.isEmpty && d.getAllDependencyArtifacts.first.getAttribute("classifier") != null)
        currentDep.put("classifier", sbtIfier(d.getAllDependencyArtifacts.first.getAttribute("classifier")))
      else currentDep.put("classifier", "")
      depList.add(currentDep)
    }

    val root = new SimpleHash
    root.put("dependencies", depList)

    val templ = config.getTemplate("/" + tmpFile.getName)
    templ.process(root, out)

    out.close
    log(Level.Info, "Converted Successfully!")

  }

  def apply(output: File, md: MD)  
  
}
