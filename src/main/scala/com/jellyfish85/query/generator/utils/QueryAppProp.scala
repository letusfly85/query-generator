package com.jellyfish85.query.generator.utils

import org.apache.commons.configuration.{PropertiesConfiguration, Configuration}
import java.util
import java.io.InputStream
import org.apache.commons.lang.StringUtils

class QueryAppProp(environment: String) {

  val inputStream: InputStream =
    getClass.getResourceAsStream("/com/jellyfish85/query/generator/define/query.properties")

  val configuration: PropertiesConfiguration =
    new PropertiesConfiguration()

  configuration.load(inputStream, "UTF8")

  /************************************************************************************************
   *
   *
   *
   ************************************************************************************************/
  var outputFolder: String = _
  val rootFolders: java.util.Iterator[String] = configuration.getKeys("folder.output.root")
  while (rootFolders.hasNext) {
    val key: String = rootFolders.next()
    if (key.replaceAll("folder.output.root.", "").equals(environment)) {
      outputFolder = configuration.getString(key)
    }
  }
  if (StringUtils.isBlank(outputFolder)) {
    throw new RuntimeException("there is no key for environment")
  }

  /************************************************************************************************
   *
   *
   *
   ************************************************************************************************/
  val tableDDLFolder:                  String   = outputFolder + configuration.getString("folder.output.erd.table")

  val indexDDLFolder:                  String   = outputFolder + configuration.getString("folder.output.erd.index")

  val procedureDDLFolder:              String   = outputFolder + configuration.getString("folder.output.erd.procedure")

  /************************************************************************************************
   *
   *
   *
   ************************************************************************************************/
  val sqlLoaderParentPath:             String   = outputFolder + configuration.getString("sql.loader.path.parent")

  val sqlLoaderCtlPath:                String   = outputFolder + configuration.getString("sql.loader.path.ctl")

  val sqlLoaderDatPath:                String   = outputFolder + configuration.getString("sql.loader.path.dat")

  val sqlLoaderLoadExecutor:           String   = configuration.getString("sql.loader.load.executor")

  val sqlLoaderColumnTimestampDefault :String   = configuration.getString("sql.loader.column.timestamp.default")

  val sqlLoaderColumnTimestampUpdate  :String   = configuration.getString("sql.loader.column.timestamp.update")

  val sqlLoaderColumnUser             :String   = configuration.getString("sql.loader.column.user")

  val sqlLoaderColumnFunction         :String   = configuration.getString("sql.loader.column.function")

  val sqlLoaderColumnLogicalDelete    :String   = configuration.getString("sql.loader.column.logical.delete")

  val sqlLoaderDefaultValueFunctionId :String   = configuration.getString("sql.loader.default.value.function.id")

  val sqlLoaderDefaultValueUserId     :String   = configuration.getString("sql.loader.default.value.user.id")

  val sqlLoaderDefaultValueTimestamp  :String   = configuration.getString("sql.loader.default.value.timestamp")

  val sqlLoaderDefaultValueCharZero   :String   = configuration.getString("sql.loader.default.value.char.zero")

  val sqlLoaderDefaultValueFile       :String   = configuration.getString("sql.loader.default.value.file")

  val sqlLoaderDefaultValueFileData   :String   = configuration.getString("sql.loader.default.value.fileData")

  val sqlLoaderDefaultValueYmd        :String   = configuration.getString("sql.loader.default.value.ymd")



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
    exceptCodeDefault.put(_key, exceptionConfiguration.getString(key))
  }

  val exceptCodeMaintenance: util.HashMap[String, String] = new util.HashMap[String, String]()
  val exceptCodeMaintenanceKeys: java.util.Iterator[String] =
    exceptionConfiguration.getKeys("except.code.maintenance")
  while (exceptCodeMaintenanceKeys.hasNext) {
    val key: String = exceptCodeMaintenanceKeys.next()
    val _key = key.replaceAll("except.code.maintenance.", "")
    exceptCodeMaintenance.put(_key, exceptionConfiguration.getString(key))
  }

  /************************************************************************************************
   * subversion path
   *
   *
   ************************************************************************************************/

  val subversionAuthorityCodeDataPath:     String
                                              = configuration.getString("subversion.authority.code.data.path")

  val subversionAuthorityCodeControlPath:  String
                                              = configuration.getString("subversion.authority.code.control.path")

  val subversionErrorCheckCodeDataPath:    String
                                              = configuration.getString("subversion.error.check.code.data.path")

  val subversionErrorCheckCodeControlPath: String
                                              = configuration.getString("subversion.error.check.code.control.path")

  val subversionProcedureDDLPathLike:      String
                                              = configuration.getString("subversion.procedure.ddl.path.like")

  /************************************************************************************************
   * workspace attributes
   *
   *
   ************************************************************************************************/
  val applicationWorkspacePath:        String   = configuration.getString("application.workspace.path")

  val applicationTemplatePath:         String   = configuration.getString("application.workspace.template.path")

  val applicationControlPath:          String   = configuration.getString("application.workspace.control.path")

  val applicationDataPath:             String   = configuration.getString("application.workspace.data.path")


  /************************************************************************************************
   * erd attributes
   *
   *
   ************************************************************************************************/
  val erdTableMasterPrefix:            String   = configuration.getString("erd.table.master.prefix")

  val erdSchemaMainName:               String   = configuration.getString("erd.schema.main.name")

  val erdSchemaSubName:                String   = configuration.getString("erd.schema.sub.name")

  val erdSchemaOuterFaceName:          String   = configuration.getString("erd.schema.outer.face.name")

  val erdSchemaOuterFaceSubName:       String   = configuration.getString("erd.schema.outer.face.sub.name")

  val erdFunctionAnotherOwnerDDL:      String   = configuration.getString("erd.function.another.owner.ddl")

}