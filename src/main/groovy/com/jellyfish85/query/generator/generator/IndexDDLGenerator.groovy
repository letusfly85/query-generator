package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndexesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsIndColumnsDao
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsIndexesDao
import com.jellyfish85.query.generator.BaseContext
import org.apache.commons.io.FilenameUtils

import java.sql.Connection

/**
 * generate index ddl and its executable scripts
 *
 *
 * @author wada shunsuke
 * @since  2013/12/01
 *
 */
class IndexDDLGenerator extends GeneralGenerator {

    private BaseContext context = null

    public IndexDDLGenerator(BaseContext _context) {
        super(_context)
        this.context = super.getBaseContext()
    }

    /**
     * generate index ddl
     *
     *
     * @param tableName
     * @param columnList
     * @param dependency
     *
     */
    public void generateIndexDDL(String tableName,
                       ArrayList<MsIndColumnsBean> columnList,
                       KrObjectDependenciesBean dependency,
                       String mode) {
        this.initializeQuery()

        String indexName   = columnList.head().indexNameAttr().value()
        String schemaName  = dependency.objectOwnerAttr().value()

        Map map = [
                schemaName  : schemaName,
                tableName   : tableName,
                indexName   : indexName,
                columnList  : columnList
        ]

        String path = ""
        if (mode.equals("drop")) {
            path = "/com/jellyfish85/query/generator/template/ddl/indexDDLWithDrop.template"
        } else {
            path = "/com/jellyfish85/query/generator/template/ddl/indexDDL.template"
        }
        this.generate(map, path)
    }

    /**
     * == generateIndexPlainDDL ==
     *
     *
     * @author wada shunsuke
     * @since  2013/12/12
     * @param tableName
     * @param columnList
     * @param dependency
     */
    public void generateIndexPlainDDL(String tableName,
                                 ArrayList<MsIndColumnsBean> columnList,
                                 KrObjectDependenciesBean dependency) {
        this.initializeQuery()

        String indexName   = columnList.head().indexNameAttr().value()
        String schemaName  = dependency.objectOwnerAttr().value()

        Map map = [
                schemaName  : schemaName,
                tableName   : tableName,
                indexName   : indexName,
                columnList  : columnList
        ]


        path = "/com/jellyfish85/query/generator/template/ddl/indexPlainDDL.template"


        this.generate(map, path)
    }

    /**
     * generate index ddls
     *
     * @author wada shunsuke
     * @since  2013/12/09
     * @param conn
     * @param fileNameHelper
     * @param dependencyGrpCd
     */
    public void generateIndexDDL(
            Connection conn,
            ArrayList<KrObjectDependenciesBean> dependencies
    ) {

        // generate dao instances
        MsIndexesDao    msIndexesDao    = new MsIndexesDao()
        MsIndColumnsDao msIndColumnsDao = new MsIndColumnsDao()

        def _list = msIndexesDao.findAll(conn)
        ArrayList<MsIndexesBean> list = msIndexesDao.convert(_list)


        // generate query each by table
        list.each {MsIndexesBean bean ->
            String tableName = bean.physicalTableNameAttr().value()
            KrObjectDependenciesBean dependency =
                    this.context.tableNameHelper.findByApplicationGroupCd(dependencies, tableName)

            def _sets = msIndColumnsDao.find(conn, bean)
            ArrayList<MsIndColumnsBean> sets = msIndColumnsDao.convert(_sets)

            if (!sets.isEmpty()) {
                this.generateIndexDDL(tableName, sets, dependency, "create")

                String indexDDLPath =
                        this.context.fileNameHelper.requestIndexDDLPath(dependency, bean)
                this.setPath(indexDDLPath)

                this.writeAppFile()
            }
        }

        generateExecuteIndexDDLShell(dependencies, list)
    }


    /**
     * generate index ddls
     *
     * @author wada shunsuke
     * @since  2013/12/09
     * @param conn
     * @param fileNameHelper
     * @param dependencyGrpCd
     */
    public void generateIndexDDL(
            Connection conn,
            ArrayList<KrObjectDependenciesBean> dependencies,
            ArrayList<String> indexes
    ) {

        // generate dao instances
        MsIndexesDao    msIndexesDao    = new MsIndexesDao()
        MsIndColumnsDao msIndColumnsDao = new MsIndColumnsDao()

        def _list = msIndexesDao.findByIndexNames(conn, indexes)
        ArrayList<MsIndexesBean> list = msIndexesDao.convert(_list)


        // generate query each by table
        list.each {MsIndexesBean bean ->
            String tableName = bean.physicalTableNameAttr().value()
            KrObjectDependenciesBean dependency =
                    this.context.tableNameHelper.findByApplicationGroupCd(dependencies, tableName)

            def _sets = msIndColumnsDao.find(conn, bean)
            ArrayList<MsIndColumnsBean> sets = msIndColumnsDao.convert(_sets)

            if (!sets.isEmpty()) {
                this.generateIndexDDL(tableName, sets, dependency, "drop")

                String indexDDLPath =
                        this.context.fileNameHelper.requestIndexDDLPath(dependency, bean)
                this.setPath(indexDDLPath)

                this.writeAppFile()
            }
        }

        generateExecuteIndexDDLShell(dependencies, list)
    }

    /**
     * generate shell scripts
     *
     * @param fileNameHelper
     * @param dependencies
     * @param list
     */
    public void generateExecuteIndexDDLShell(
            ArrayList<KrObjectDependenciesBean> dependencies,
            ArrayList<MsIndexesBean> list) {

        this.initializeQuery()

        String executeShellPath =
                this.context.fileNameHelper.requestExecuteIndexDDLShellPath()
        this.setPath(executeShellPath)

        list.each {MsIndexesBean bean ->
            String tableName = bean.physicalTableNameAttr().value()
            KrObjectDependenciesBean dependency =
                    this.context.tableNameHelper.findByApplicationGroupCd(dependencies, tableName)

            String indexDDLPath =
                    this.context.fileNameHelper.requestIndexDDLPath(dependency, bean)
            String ddlName = FilenameUtils.getName(indexDDLPath)

            this.appendQuery("@" + ddlName + "\n")
        }

        this.writeAppFile()
    }

}
