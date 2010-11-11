package com.weiglewilczek.mvn2sbt

object Mvn2SbtHelpers {

  /* Builds the classifier the SBT way.
   * @param input Takes a String e.g. "classifier "\"javadoc\"".
   * @TODO Should not take the whole String, just the classifier itself!
   */
  def sbtIfier(input: String): String = input match {
    case javadoc if (input == " classifier " + "\"javadoc\"") => " withJavadoc"
    case source if (input == " classifier " + "\"sources\"") => " withSources"
    case _ => input
  }

}
