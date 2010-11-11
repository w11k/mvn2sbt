package com.weiglewilczek.mvn2sbt

object Mvn2SbtHelpers {

  /**
   * Builds the classifier the SBT way.
   * @param input Takes a classifier.
   */
  def sbtIfier(input: String): String = input match {
    case j if (j == "javadoc") => " withJavadoc"
    case s if (s == "sources") => " withSources"
    case _ => " classifier " + "\"" + input + "\""
  }

  /**
   * Builds the %% for getting the Scala Version within SBT.
   * @param input The getDependencyRevisionId.getName.
   * @TODO Make it pretty and other methodname!
   */
  def convertTodoublePercent(input: String): String = input match {
    case a if (a.endsWith("_2.7.7") || a.endsWith("_2.8.0") || a.endsWith("_2.8.1")) =>
      " %% \"" + a.reverse.dropWhile(_ != '_').toArray.drop(1).reverse.mkString + "\""
    case _ =>
      " % \"" + input + "\""
  }

}
