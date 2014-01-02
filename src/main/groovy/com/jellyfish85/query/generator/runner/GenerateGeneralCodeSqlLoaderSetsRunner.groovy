package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTabColumnsDao
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.generator.GeneralCodeGenerator
import com.jellyfish85.query.generator.helper.AppFileNameHelper
import com.jellyfish85.query.generator.helper.CodeGeneratorHelper
import com.jellyfish85.query.generator.helper.TableNameHelper
import com.jellyfish85.query.generator.utils.QueryAppProp
import com.jellyfish85.xlsaccessor.utils.XlsAppProp

/**
 * == GenerateGeneralCodeSqlLoaderSetsRunner ==
 *
 *
 * @author wada shunsuke
 * @since  2013/12/08
 *
 */
class GenerateGeneralCodeSqlLoaderSetsRunner {

    public static void main(String[] args) {
        def dependencyGrpCd = args[0]
        def environment     = args[1]

        BaseRunner  runner  = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context = runner._context
        runner.databaseInitialize()

        def conn            = runner.getConnection()
        def queryProp       = context.queryProp
        def fileNameHelper  = context.fileNameHelper
        def tableNameHelper = context.tableNameHelper

        XlsAppProp      xlsAppProp       = new XlsAppProp()

        String generalTableName = xlsAppProp.generalCodePhysicalTableName()

        KrObjectDependenciesDao dependenciesDao = new KrObjectDependenciesDao()
        def _dependencies = dependenciesDao.findByDependencyGrpCd(conn, dependencyGrpCd)
        ArrayList<KrObjectDependenciesBean> dependencies = dependenciesDao.convert(_dependencies)

        String schemaName =
                tableNameHelper.findByApplicationGroupCd(dependencies, generalTableName).
                        objectOwnerAttr().value()

        MsTablesBean tablesBean = new MsTablesBean()
        tablesBean.physicalTableNameAttr().setValue(generalTableName)

        MsTabColumnsDao columnsDao = new MsTabColumnsDao()
        def _columnList = columnsDao.find(conn, tablesBean)
        ArrayList<MsTabColumnsBean> columnList = columnsDao.convert(_columnList)

        GeneralCodeGenerator generator = new GeneralCodeGenerator(context)
        generator.generateDataFile()
        generator.generateControlFile(schemaName, columnList)

        HashMap<String, String> tableNames = new HashMap<>()
        tableNames.put(generalTableName, queryProp.sqlLoaderLoadExecutor())
        CodeGeneratorHelper helper = new CodeGeneratorHelper(context)
        helper.generateLoadingShellScript(tableNames, fileNameHelper.requestSqlLoaderPath4GeneralCode())

        runner.databaseFinalize()
    }
}