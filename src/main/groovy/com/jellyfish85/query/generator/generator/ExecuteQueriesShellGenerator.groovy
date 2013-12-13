package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import org.apache.commons.io.FilenameUtils

/**
 * == ExecuteQueriesShellGenerator ==
 *
 *
 * @author wada shunsuke
 * @since  2013/12/13
 *
 */
class ExecuteQueriesShellGenerator extends GeneralGenerator {

    /**
     * == generateExecuteQueriesShell ==
     *
     *
     * @author wada shunsuke
     * @since  2013/12/13
     * @param msTablesBeanArrayList
     * @param dependency
     */
    public void generateExecuteQueriesShell(
            ArrayList<MsTablesBean>  msTablesBeanArrayList,
            KrObjectDependenciesBean dependency
    ) {

        ArrayList<String> execScriptsList = new ArrayList<String>()
        msTablesBeanArrayList.each {MsTablesBean msTablesBean ->
            String execScriptsPath =
                    this.fileNameHelper.requestExecuteQueriesPath(dependency, msTablesBean)

            String execScriptsName = FilenameUtils.getName(execScriptsPath)
            execScriptsList.add(execScriptsName)
        }

        Map map = [
                execScriptsList : execScriptsList
        ]

        String path = "/com/jellyfish85/query/generator/template/ddl/ExecuteAllQueries.template"

        this.generate(map, path)
        String executeAllQueriesPath = this.fileNameHelper.requestExecuteAllQueriesPath()
        this.setPath(executeAllQueriesPath)
        this.writeAppFile()
    }

    /**
     * == generateExecuteQueriesShell ==
     *
     *
     * @author wada shunsuke
     * @since  2013/12/13
     * @param msTablesBean
     * @param dependency
     */
    public void generateExecuteQueriesShell(
            MsTablesBean             msTablesBean,
            KrObjectDependenciesBean dependency
    ) {
        this.initializeQuery()

        String renameQueryPath =
                this.fileNameHelper.requestRenamePath(dependency, msTablesBean)
        String restoreQueryPath =
                this.fileNameHelper.requestRestorePath(dependency, msTablesBean)
        String tableDDLPath =
                this.fileNameHelper.requestTableDDLPath(dependency, msTablesBean)
        String dropBackupTableQueryPath =
                this.fileNameHelper.requestDropBackupTableQueryPath(dependency, msTablesBean)

        String renameQueryName  = FilenameUtils.getName(renameQueryPath)
        String restoreQueryName = FilenameUtils.getName(restoreQueryPath)
        String tableDDLName     = FilenameUtils.getName(tableDDLPath)
        String dropBackupTableQueryName =
                FilenameUtils.getBaseName(dropBackupTableQueryPath)

        Map map = [
                renameQueryName          : renameQueryName,
                restoreQueryName         : restoreQueryName,
                tableDDLName             : tableDDLName,
                dropBackupTableQueryName : dropBackupTableQueryName
        ]

        String path = "/com/jellyfish85/query/generator/template/ddl/ExecuteQueries.template"

        this.generate(map, path)
        String executeQueriesPath =
                this.fileNameHelper.requestExecuteQueriesPath(dependency, msTablesBean)
        this.setPath(executeQueriesPath)
        this.writeAppFile()

    }

}
