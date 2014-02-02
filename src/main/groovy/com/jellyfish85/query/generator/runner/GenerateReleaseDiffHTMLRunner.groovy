package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.manager.DatabaseManager
import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.generator.ReleaseDiffHTMLGenerator
import com.jellyfish85.query.generator.utils.QueryAppProp

import java.sql.Connection

class GenerateReleaseDiffHTMLRunner {

    /**
     * usage:
     *  GenerateReleaseDiffHTMLRunner -Prunargs=01,product/operation/training/4user
     *  GenerateReleaseDiffHTMLRunner -Prunargs=02,develop
     *
     *
     * @param args
     */
    public static void main(String[] args){
        def dependencyGrpCd = args[0]
        def environment     = args[1]

        BaseRunner  runner     = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context    = runner._context
        QueryAppProp queryProp = context.queryProp

        runner.databaseInitialize()
        Connection conn = runner.getConnection()

        DatabaseManager db = new DatabaseManager()
        db.myConnect()
        Connection myConn = db.conn()

        ReleaseDiffHTMLGenerator generator =
                new ReleaseDiffHTMLGenerator(context, queryProp.subversionTagName(), conn, myConn)

        generator.generateHTML()
    }
}
