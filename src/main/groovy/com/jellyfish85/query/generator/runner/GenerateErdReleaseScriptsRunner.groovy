package com.jellyfish85.query.generator.runner

import com.jellyfish85.query.generator.BaseContext
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
        def preReleaseId    = args[2]
        //def curReleaseId    = args[3]

        BaseRunner  runner  = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context = runner._context
        runner.databaseInitialize()

        def conn         = runner.getConnection()
        def dependencies = runner.getDependencies()
        def queryProp    = context.queryProp

        ErdReleaseScriptsGenerator generator = new ErdReleaseScriptsGenerator(context, preReleaseId)
        generator.generateErdReleaseScripts(conn, dependencies)

        // add login sql to parent folder
        ResourceCopyHelper copyHelper = new ResourceCopyHelper()
        String loginSqlPath = (new File(queryProp.tableDDLFolder(), "login.sql")).getPath()
        copyHelper.copyLoginSql(loginSqlPath)

        runner.databaseFinalize()
    }
}