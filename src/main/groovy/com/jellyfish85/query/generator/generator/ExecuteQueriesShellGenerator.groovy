package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.BaseContext
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

    private BaseContext context = null
    public ExecuteQueriesShellGenerator(BaseContext _context) {
        super(_context)
        this.context = super.getBaseContext()
    }

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
            ArrayList<MsTablesBean>             msTablesBeanArrayList,
            ArrayList<KrObjectDependenciesBean> dependencies
    ) {

        ArrayList<String> execScriptsList = new ArrayList<String>()
        msTablesBeanArrayList.each {MsTablesBean tablesBean ->
            KrObjectDependenciesBean dependency =
                    this.context.tableNameHelper.
                            findByApplicationGroupCd(dependencies, tablesBean.physicalTableNameAttr().value())

            String execScriptsPath =
                    this.context.fileNameHelper.requestExecuteQueriesPath(dependency, tablesBean)

            String execScriptsName = FilenameUtils.getName(execScriptsPath)
            execScriptsList.add(execScriptsName)
        }

        Map map = [
                execScriptsList : execScriptsList
        ]

        String path = "/com/jellyfish85/query/generator/template/ddl/ExecuteAllQueries.template"

        this.generate(map, path)
        String executeAllQueriesPath = this.context.fileNameHelper.requestExecuteAllQueriesPath()
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
                this.context.fileNameHelper.requestRenamePath(dependency, msTablesBean)
        String restoreQueryPath =
                this.context.fileNameHelper.requestRestorePath(dependency, msTablesBean)
        String tableDDLPath =
                this.context.fileNameHelper.requestTableDDLPath(dependency, msTablesBean)
        String dropBackupTableQueryPath =
                this.context.fileNameHelper.requestDropBackupTableQueryPath(dependency, msTablesBean)

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
                this.context.fileNameHelper.requestExecuteQueriesPath(dependency, msTablesBean)
        this.setPath(executeQueriesPath)
        this.writeAppFile()

    }

}
