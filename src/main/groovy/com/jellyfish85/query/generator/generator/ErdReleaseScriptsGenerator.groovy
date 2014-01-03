package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndexesBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsIndColumnsDao
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsIndexesDao
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTabColumnsDao
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTablesDao
import com.jellyfish85.query.generator.BaseContext
import org.apache.commons.lang.ArrayUtils

import java.sql.Connection

class ErdReleaseScriptsGenerator extends GeneralGenerator {

    private BaseContext context      = null
    private BigDecimal  preReleaseId = null

    private ExecuteQueriesShellGenerator  executeQueriesShellGenerator  = null
    private RenameQueryGenerator          renameQueryGenerator          = null
    private RestoreQueryGenerator         restoreQueryGenerator         = null
    private TableDDLGenerator             tableDDLGenerator             = null
    private DropBackupTableQueryGenerator dropBackupTableQueryGenerator = null

    /**
     *
     *
     *
     * @param _context
     * @param _preReleaseId
     */
    public ErdReleaseScriptsGenerator(BaseContext _context, String _preReleaseId) {
        super(_context)
        this.context = super.getBaseContext()

        executeQueriesShellGenerator  = new ExecuteQueriesShellGenerator(_context)
        renameQueryGenerator          = new RenameQueryGenerator(_context)
        restoreQueryGenerator         = new RestoreQueryGenerator(_context)
        tableDDLGenerator             = new TableDDLGenerator(_context)
        dropBackupTableQueryGenerator = new DropBackupTableQueryGenerator(_context)

        this.preReleaseId = new BigDecimal(_preReleaseId)
    }

    private MsTablesDao     msTablesDao     = new MsTablesDao()
    private MsTabColumnsDao msTabColumnsDao = new MsTabColumnsDao()
    private MsIndexesDao    msIndexesDao    = new MsIndexesDao()
    private MsIndColumnsDao msIndColumnsDao = new MsIndColumnsDao()

    /**
     *
     *
     * @param conn
     * @param dependencies
     */
    public void generateErdReleaseScripts(
            Connection                          conn,
            ArrayList<KrObjectDependenciesBean> dependencies
    ) {

        def _tableList = msTablesDao.findByReleaseId(conn, this.preReleaseId)
        ArrayList<MsTablesBean> tableList = msTablesDao.convert(_tableList)

        // generate query each by table
        tableList.each {MsTablesBean bean ->
            String tableName = bean.physicalTableNameAttr().value()
            println(".. generating .. ${tableName} .. queries")

            KrObjectDependenciesBean dependency =
                    this.context.tableNameHelper.findByApplicationGroupCd(dependencies, tableName)

            def _indList = msIndexesDao.findByTableId(conn, bean.tableIdAttr().value())
            HashMap<MsIndexesBean, ArrayList<MsIndColumnsBean>> hashMap =
                    new HashMap<MsIndexesBean, ArrayList<MsIndColumnsBean>>()
            ArrayList<MsIndexesBean> indList = msIndexesDao.convert(_indList)

            if (!ArrayUtils.isEmpty(indList)) {
                indList.each {MsIndexesBean indexesBean ->
                    def _indColList = msIndColumnsDao.find(conn, indexesBean)
                    ArrayList<MsIndColumnsBean> indColList = msIndColumnsDao.convert(_indColList)

                    hashMap.put(indexesBean, indColList)
                }
            }

            def _sets = msTabColumnsDao.find(conn, bean)
            ArrayList<MsTabColumnsDao> sets = msTabColumnsDao.convert(_sets)

            ArrayList<MsTablesBean> targetList = new ArrayList<MsTablesBean>()
            if (!sets.isEmpty()) {
                this.renameQueryGenerator.generateRenameQuery(bean, dependency)
                this.restoreQueryGenerator.generateRestoreQuery(bean, dependency)
                this.tableDDLGenerator.generateTableDDL(bean, sets, dependency, hashMap)
                this.dropBackupTableQueryGenerator.generateDropBackupTableQuery(bean, dependency)

                this.executeQueriesShellGenerator.generateExecuteQueriesShell(bean, dependency)

                targetList.add(bean)
            }
            this.executeQueriesShellGenerator.
                    generateExecuteQueriesShell(targetList, dependency)
        }
    }
}
