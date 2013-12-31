package com.jellyfish85.query.generator.utils

import org.apache.commons.configuration.{PropertiesConfiguration, Configuration}
import java.util

class QueryAppProp {

  val configuration: Configuration =
    new PropertiesConfiguration("com/jellyfish85/query/generator/define/query.properties")


  val restoreFolder:                   String   = configuration.getString("tableScriptFolder")

  val tableDDLFolder:                  String   = configuration.getString("tableScriptFolder")

  val sqlLoaderLoadExecutor:           String   = configuration.getString("sql.loader.load.executor")

  val sqlLoaderParentPath:             String   = configuration.getString("sql.loader.path.parent")

  val sqlLoaderCtlPath:                String   = configuration.getString("sql.loader.path.ctl")

  val sqlLoaderDatPath:                String   = configuration.getString("sql.loader.path.dat")

  val indexDDLFolder:                  String   = configuration.getString("indexDDLFolder")

  val applicationWorkspacePath:        String   = configuration.getString("application.workspace.path")

  val sqlLoaderColumnTimestampDefault :String   = configuration.getString("sql.loader.column.timestamp.default")

  val sqlLoaderColumnTimestampUpdate  :String   = configuration.getString("sql.loader.column.timestamp.update")

  val sqlLoaderColumnUser             :String   = configuration.getString("sql.loader.column.user")

  val sqlLoaderColumnFunction         :String   = configuration.getString("sql.loader.column.function")

  val sqlLoaderDefaultValueFunctionId :String   = configuration.getString("sql.loader.default.value.function.id")

  val sqlLoaderDefaultValueUserId     :String   = configuration.getString("sql.loader.default.value.user.id")

  val sqlLoaderDefaultValueTimestamp  :String   = configuration.getString("sql.loader.default.value.timestamp")

  /************************************************************************************************
   *
   *
   *
   ************************************************************************************************/
  val exceptionConfiguration: Configuration =
    new PropertiesConfiguration("com/jellyfish85/query/generator/define/exceptional.code.properties")

  val exceptCodeDefault: util.HashMap[String, String] = new util.HashMap[String, String]()
  val exceptCodeDefaultKeys: java.util.Iterator[String] =
    exceptionConfiguration.getKeys("except.code.default")
  while (exceptCodeDefaultKeys.hasNext) {
    val key: String = exceptCodeDefaultKeys.next()
    val _key = key.replaceAll("except.code.default.", "")
    exceptCodeDefault.put(_key, configuration.getString(key))
  }

  val exceptCodeMaintenance: util.HashMap[String, String] = new util.HashMap[String, String]()
  val exceptCodeMaintenanceKeys: java.util.Iterator[String] =
    exceptionConfiguration.getKeys("except.code.maintenance")
  while (exceptCodeMaintenanceKeys.hasNext) {
    val key: String = exceptCodeMaintenanceKeys.next()
    val _key = key.replaceAll("except.code.maintenance.", "")
    exceptCodeMaintenance.put(_key, configuration.getString(key))
  }
}