import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) {
<#list dependencies as dep>
  val ${dep.depName} = ${dep.organisation} ${dep.name} ${dep.revision} <#if dep.moduleConfig != "">${dep.moduleConfig}</#if> <#if dep.classifier != "">${dep.classifier}</#if>
</#list>

}