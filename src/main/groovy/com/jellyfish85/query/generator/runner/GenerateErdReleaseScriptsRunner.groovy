package com.jellyfish85.query.generator.runner

import com.jellyfish85.query.generator.generator.ErdReleaseScriptsGenerator

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

        String dependencyGrpCd          = args[0]
        ArrayList<String> tableNameList = argsHelper.requestTableNameList(args[1])

        ErdReleaseScriptsGenerator generator = new ErdReleaseScriptsGenerator()
        generator.generateErdReleaseScripts(conn, dependencyGrpCd, tableNameList)

        _context.databaseFinalize()
    }
}
