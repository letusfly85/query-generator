package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTablesDao
import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.generator.ErdReleaseScriptsGenerator
import com.jellyfish85.query.generator.helper.ResourceCopyHelper

class GeneratePatchErdReleaseScriptsRunner {

    public static void main(String[] args) {
        def dependencyGrpCd = args[0]
        def environment     = args[1]
        def tableList       = args.tail().tail()

        BaseRunner  runner  = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context = runner._context
        runner.databaseInitialize()

        def conn         = runner.getConnection()
        def dependencies = runner.getDependencies()
        def queryProp    = context.queryProp

        ErdReleaseScriptsGenerator generator = new ErdReleaseScriptsGenerator(context)

        MsTablesDao msTablesDao = new MsTablesDao()
        ArrayList<String> tableNames = new ArrayList<>()
        tableList.each {tableName -> tableNames.add(tableName)}
        def _tableList = msTablesDao.findByTableNames(conn, tableNames)
        ArrayList<MsTablesBean> msTablesBeans = msTablesDao.convert(_tableList)

        generator.generateErdReleaseScripts(conn, dependencies, msTablesBeans)
        generator.generateBIRevertErdReleaseScripts(conn, dependencies, msTablesBeans)

        // add login sql to parent folder
        ResourceCopyHelper copyHelper = new ResourceCopyHelper()
        String loginSqlPath = (new File(queryProp.tableDDLFolder(), "login.sql")).getPath()
        copyHelper.copyLoginSql(loginSqlPath)

        runner.databaseFinalize()
    }
}
