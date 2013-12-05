package com.jellyfish85.query.generator.utils

import java.util.Properties
import java.io.InputStream

class AppProp {

  var inputStream: InputStream = _

  var property: Properties     = _

  var outputHome: String = {
    if (System.getProperties("out.home") != null) {
      this.outputHome = System.getProperties("out.home")

    } else {
      this.outputHome = "output"
    }
  }

  def load {
    inputStream = getClass.getResourceAsStream("/com/jellyfish85/query/generator/define/query.properties")
    property.load(inputStream)
  }

  var restoreFolder: String    = property.getProperty("restoreFolder")

}