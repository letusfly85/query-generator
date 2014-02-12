package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTablesDao
import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.constant.QueryAppConst
import com.jellyfish85.query.generator.generator.ErdReleaseScriptsGenerator
import com.jellyfish85.query.generator.helper.ResourceCopyHelper

/**
 * == GenerateErdReleaseScriptsRunner ==
 *
 * @author wada shunsuke
 * @since  2013/12/05
 *
 */
class GenerateErdReleaseScriptsRunner {

    public static void main(String[] args) {
        def dependencyGrpCd = args[0]
        def environment     = args[1]
        def _preReleaseId    = args[2]
        BigDecimal preReleaseId = new BigDecimal(_preReleaseId)
        //def curReleaseId    = args[3]

        BaseRunner  runner  = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context = runner._context
        runner.databaseInitialize()

        def conn         = runner.getConnection()
        def dependencies = runner.getDependencies()
        def queryProp    = context.queryProp

        ErdReleaseScriptsGenerator generator = new ErdReleaseScriptsGenerator(context)
        generator.setPreReleaseId(preReleaseId)

        MsTablesDao msTablesDao = new MsTablesDao()
        def _tableList = msTablesDao.findByReleaseId(conn, preReleaseId)
        ArrayList<MsTablesBean> tableList = msTablesDao.convert(_tableList)
        ArrayList<MsTablesBean> tables = new ArrayList<>()
        tableList.findAll {MsTablesBean bean ->
            def tableName =  bean.physicalTableNameAttr().value()
            def group     =  context.tableNameHelper.identifyTableGroup(tableName)

            if (group != QueryAppConst.APPLICATION_GROUP_BI) {
                tables.add(bean)
            }
        }
        generator.generateErdReleaseScripts(conn, dependencies, tables)

        // add login sql to parent folder
        ResourceCopyHelper copyHelper = new ResourceCopyHelper()
        String loginSqlPath = (new File(queryProp.tableDDLFolder(), "login.sql")).getPath()
        copyHelper.copyLoginSql(loginSqlPath)

        runner.databaseFinalize()
    }
}