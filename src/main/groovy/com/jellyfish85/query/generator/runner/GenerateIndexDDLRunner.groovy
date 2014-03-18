package com.jellyfish85.query.generator.runner

import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.generator.IndexDDLGenerator
import com.jellyfish85.query.generator.helper.ResourceCopyHelper

/**
 * generate index ddls
 *
 *
 * @author wada shunsuke
 * @since  2013/12/09
 *
 */
class GenerateIndexDDLRunner {

    public static void main(String[] args) {
        def dependencyGrpCd = args[0]
        def environment     = args[1]
        def serviceName     = args[2]

        BaseRunner  runner  = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context = runner._context
        runner.databaseInitialize()

        def conn      = runner.getConnection()
        def queryProp = context.queryProp

        // generate query generator instance
        IndexDDLGenerator generator = new IndexDDLGenerator(context)

        // generate files
        generator.generateIndexDDL(conn, serviceName, runner.getDependencies())

        // add login sql to parent folder
        ResourceCopyHelper copyHelper = new ResourceCopyHelper()
        String loginSqlPath = (new File(queryProp.indexDDLFolder(), "login.sql")).getPath()
        copyHelper.copyLoginSql(loginSqlPath)

        runner.databaseFinalize()
    }
}
