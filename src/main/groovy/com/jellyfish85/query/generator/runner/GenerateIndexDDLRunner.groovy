package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndexesBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsIndColumnsDao
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsIndexesDao
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.generator.IndexDDLGenerator
import com.jellyfish85.query.generator.helper.AppFileNameHelper
import com.jellyfish85.query.generator.helper.TableNameHelper

/**
 * == GenerateIndexDDLRunner ==
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

        BaseRunner  runner  = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context = runner._context
        runner.databaseInitialize()

        def conn = runner.getConnection()
        def fileNameHelper  = context.fileNameHelper

        // generate query generator instance
        IndexDDLGenerator generator = new IndexDDLGenerator(context)

        // generate files
        generator.generateIndexDDL(conn, fileNameHelper, dependencyGrpCd)

        runner.databaseFinalize()
    }
}
