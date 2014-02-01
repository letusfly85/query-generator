package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.manager.DatabaseManager
import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.generator.ReleaseDiffHTMLGenerator

import java.sql.Connection

/**
 * Created with IntelliJ IDEA.
 * User: wada-s
 * Date: 14/02/01
 * Time: 23:25
 */
class GenerateReleaseDiffHTMLRunner {

    public static void main(String[] args){
        def dependencyGrpCd = args[0]
        def environment     = args[1]
        def tagName         = args[2]

        BaseRunner  runner  = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context = runner._context

        runner.databaseInitialize()
        Connection conn = runner.getConnection()

        DatabaseManager db = new DatabaseManager()
        db.myConnect()
        Connection myConn = db.conn()

        ReleaseDiffHTMLGenerator generator =
                new ReleaseDiffHTMLGenerator(context, tagName, conn, myConn)

        generator.generateHTML()
    }
}
