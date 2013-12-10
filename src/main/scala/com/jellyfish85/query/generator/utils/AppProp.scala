package com.jellyfish85.query.generator.utils

import java.util.Properties
import java.io.InputStream

class AppProp {

  var inputStream: InputStream = _

  var property: Properties     = new Properties()

  var outputHome: String  = _

  def setOutputHome {
    if (System.getProperty("out.home") != null) {
      this.outputHome = System.getProperty("out.home")

    } else {
      this.outputHome = "output"
    }
  }

  def load {
    setOutputHome
    inputStream = getClass.getResourceAsStream("/com/jellyfish85/query/generator/define/query.properties")
    property.load(inputStream)
  }

  def restoreFolder:   String    = property.getProperty("tableScriptFolder")
  def tableDDLFolder:  String    = property.getProperty("tableScriptFolder")

  def sqlLoaderFolder: String    = property.getProperty("sqlLoaderFolder")

  def indexDDLFolder:  String    = property.getProperty("indexDDLFolder")

}