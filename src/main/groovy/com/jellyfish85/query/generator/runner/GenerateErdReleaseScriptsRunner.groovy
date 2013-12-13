package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTabColumnsDao
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTablesDao
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.query.generator.generator.ErdReleaseScriptsGenerator
import com.jellyfish85.query.generator.generator.RenameQueryGenerator
import com.jellyfish85.query.generator.generator.RestoreQueryGenerator
import com.jellyfish85.query.generator.generator.TableDDLGenerator
import com.jellyfish85.query.generator.helper.AppFileNameHelper
import com.jellyfish85.query.generator.helper.TableNameHelper

/**
 * == GenerateErdReleaseScriptsRunner ==
 *
 * @author wada shunsuke
 * @since  2013/12/05
 *
 */
class GenerateErdReleaseScriptsRunner {

    public static void main(String[] args) {

        BaseRunner _context = new BaseRunner()
        _context.databaseInitialize()

        def conn       = _context.getConnection()
        def argsHelper = _context.getArgsHelper()
        AppFileNameHelper fileNameHelper = new AppFileNameHelper()

        String dependencyGrpCd          = args[0]
        ArrayList<String> tableNameList = argsHelper.requestTableNameList(args[1])

        ErdReleaseScriptsGenerator generator = new ErdReleaseScriptsGenerator()
        generator.generateErdReleaseScripts(conn, dependencyGrpCd, tableNameList)

        _context.databaseFinalize()
    }
}
