package com.jellyfish85.query.generator.runner

import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.register.ReleaseDiffRegister
import com.jellyfish85.query.generator.utils.QueryAppProp

import java.sql.Connection

class RegisterReleaseDiffRunner {

    /**
     *
     * usage:
     *  gradle RegisterReleaseDiffRunner -Prunargs=01,product/operation/training/4user
     *  gradle RegisterReleaseDiffRunner -Prunargs=02,develop,develop
     *
     * @param args
     */
    public static void main(String[] args){
        def dependencyGrpCd = args[0]
        def environment     = args[1]
        def envName         = args[2]

        BaseRunner  runner     = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context    = runner._context
        QueryAppProp queryProp = context.queryProp

        runner.databaseInitialize()
        Connection conn = runner.getConnection()

        ReleaseDiffRegister register =
                new ReleaseDiffRegister(conn, queryProp.subversionTagName(), envName)

        register.setAttributeFromDB()

        register.setSVNAttributeFromSVN()

        register.register()
    }

}
