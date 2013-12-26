package com.jellyfish85.query.generator.utils

import org.apache.commons.configuration.{PropertiesConfiguration, Configuration}

class QueryAppProp {

  val configuration: Configuration =
    new PropertiesConfiguration("com/jellyfish85/query/generator/define/query.properties")


  val restoreFolder:            String          = configuration.getString("tableScriptFolder")

  val tableDDLFolder:           String          = configuration.getString("tableScriptFolder")

  val sqlLoaderParentPath:      String          = configuration.getString("sql.loader.path.parent")

  val sqlLoaderCtlPath:         String          = configuration.getString("sql.loader.path.ctl")

  val sqlLoaderDatPath:         String          = configuration.getString("sql.loader.path.dat")

  val indexDDLFolder:           String          = configuration.getString("indexDDLFolder")

  val applicationWorkspacePath: String          = configuration.getString("application.workspace.path")

  val sqlLoaderColumnTimestampDefault :String   = configuration.getString("sql.loader.column.timestamp.default")

  val sqlLoaderColumnTimestampUpdate  :String   = configuration.getString("sql.loader.column.timestamp.update")

  val sqlLoaderColumnUser             :String   = configuration.getString("sql.loader.column.user")

  val sqlLoaderColumnFunction         :String   = configuration.getString("sql.loader.column.function")




}