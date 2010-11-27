import sbt._

class Project(info: ProjectInfo) extends DefaultWebProject(info) {
   override def libraryDependencies = Set(
<#list dependencies as dep>
  ${dep.organisation} ${dep.name} ${dep.revision} <#if dep.moduleConfig != "">${dep.moduleConfig}</#if> <#if dep.classifier != "">${dep.classifier}</#if>,
</#list>
   ) ++ super.libraryDependencies
}
