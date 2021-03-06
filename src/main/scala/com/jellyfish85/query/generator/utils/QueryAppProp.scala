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

  val objectCheckFolder:               String   = outputFolder + configuration.getString("folder.output.erd.check")

  val htmlReleaseDiffFolder:           String   = outputFolder + configuration.getString("folder.output.html.release.diff")

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

  val redmineURLRevisionHeader:            String
                                              = configuration.getString("redmine.url.revision.header")

  val subversionProjectName:               String
                                              = configuration.getString("subversion.project.name")

  val subversionRepositoryCode:            String
                                              = configuration.getString("subversion.repository.code")

  val subversionProcedureDDLPathLike:      String
                                              = configuration.getString("subversion.procedure.ddl.path.like")

  val subversionUrlTag4Test:               String
                                              = configuration.getString("subversion.url.tag.4test")

  var subversionCurrentName:                   String = _
  val subversionCurrentNames: java.util.Iterator[String] = configuration.getKeys("subversion.url.current")
  while (subversionCurrentNames.hasNext) {
    val key: String = subversionCurrentNames.next()
    if (key.replaceAll("subversion.url.current.", "").equals(environment)) {
      subversionCurrentName = configuration.getString(key)
    }
  }

  var subversionTagName:                   String = _
  val subversionTagNames: java.util.Iterator[String] = configuration.getKeys("subversion.url.tag")
  while (subversionTagNames.hasNext) {
    val key: String = subversionTagNames.next()
    if (key.replaceAll("subversion.url.tag.", "").equals(environment)) {
      subversionTagName = configuration.getString(key)
    }
  }

  var subversionTagHeader:                   String = _
  val subversionTagHeaders: java.util.Iterator[String] = configuration.getKeys("subversion.url.tag.header")
  while (subversionTagHeaders.hasNext) {
    val key: String = subversionTagHeaders.next()
    if (key.replaceAll("subversion.url.tag.header.", "").equals(environment)) {
      subversionTagHeader = configuration.getString(key)
    }
  }

  //todo check how to control revisions when using tags and branches
  val subversionGeneralCodeBookPath:      String =
                subversionTagHeader + configuration.getString("subversion.general.code.book.path")

  val subversionAuthorityCodeDataPath:     String =
                subversionCurrentName + configuration.getString("subversion.authority.code.data.path")

  val subversionAuthorityCodeControlPath:  String =
                subversionCurrentName + configuration.getString("subversion.authority.code.control.path")

  val subversionErrorCheckCodeDataPath:    String =
                subversionCurrentName + configuration.getString("subversion.error.check.code.data.path")

  val subversionErrorCheckCodeControlPath: String =
                subversionCurrentName + configuration.getString("subversion.error.check.code.control.path")

  val subversionUniqueCodeBookParentPath: String =
                subversionCurrentName + configuration.getString("subversion.unique.code.book.parentPath")

  val subversionTemplateRecordBookPath: String =
                subversionCurrentName + configuration.getString("subversion.template.record.book.path")

  val subversionTemplateRecordDocsPath: String =
                subversionCurrentName + configuration.getString("subversion.template.record.docs.path")

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

  val erdSidName:                      String   = configuration.getString("erd.sid.name")

  val erdSchemaAdminName:              String   = configuration.getString("erd.schema.admin.name")

  val erdSchemaAdminPass:              String   = configuration.getString("erd.schema.admin.pass")

  val erdSchemaMainName:               String   = configuration.getString("erd.schema.main.name")

  val erdSchemaSubName:                String   = configuration.getString("erd.schema.sub.name")

  val erdSchemaBIMainName:             String   = configuration.getString("erd.schema.bi.main.name")

  val erdSchemaBISubName:              String   = configuration.getString("erd.schema.bi.sub.name")

  val erdSchemaOuterFaceName:          String   = configuration.getString("erd.schema.outer.face.name")

  val erdSchemaOuterFaceSubName:       String   = configuration.getString("erd.schema.outer.face.sub.name")

  val erdSchema4Test:                  String   = configuration.getString("erd.schema.4test")

  val erdFunctionAnotherOwnerDDL:      String   = configuration.getString("erd.function.another.owner.ddl")

  val erdServiceNameMain:              String   = configuration.getString("erd.service.name.Main")

  val erdServiceNameBI:                String   = configuration.getString("erd.service.name.BI")


  val exceptionPg: String = configuration.getString("exception.pg")

}