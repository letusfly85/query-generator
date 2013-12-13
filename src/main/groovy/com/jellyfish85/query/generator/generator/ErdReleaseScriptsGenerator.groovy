package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndexesBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsIndColumnsDao
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsIndexesDao
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTabColumnsDao
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTablesDao
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.query.generator.helper.TableNameHelper
import org.apache.commons.lang.ArrayUtils

import java.sql.Connection

class ErdReleaseScriptsGenerator {

    private TableNameHelper tableNameHelper = new TableNameHelper()

    private RenameQueryGenerator  renameQueryGenerator  = new RenameQueryGenerator()
    private RestoreQueryGenerator restoreQueryGenerator = new RestoreQueryGenerator()
    private TableDDLGenerator     tableDDLGenerator     = new TableDDLGenerator()
    private DropBackupTableQueryGenerator dropBackupTableQueryGenerator =
            new DropBackupTableQueryGenerator()

    public void generateErdReleaseScripts(
            Connection              conn,
            String                  dependencyGrpCd,
            ArrayList<String>       tableNameList
    ) {

        // generate dao instances
        KrObjectDependenciesDao krObjectDependenciesDao =
                new KrObjectDependenciesDao()
        MsTablesDao     msTablesDao     = new MsTablesDao()
        MsTabColumnsDao msTabColumnsDao = new MsTabColumnsDao()
        MsIndexesDao    msIndexesDao    = new MsIndexesDao()
        MsIndColumnsDao msIndColumnsDao = new MsIndColumnsDao()


        // specify dependencies
        def _dependencies = krObjectDependenciesDao.findByDependencyGrpCd(conn, dependencyGrpCd)
        ArrayList<KrObjectDependenciesBean> dependencies =
                krObjectDependenciesDao.convert(_dependencies)

        def _tableList = msTablesDao.findByTableNames(conn, tableNameList)
        ArrayList<MsTablesBean> tableList = msTablesDao.convert(_tableList)

        // generate query each by table
        tableList.each {MsTablesBean bean ->
            String tableName = bean.physicalTableNameAttr().value()
            println(".. generating .. ${tableName} .. queries")

            KrObjectDependenciesBean dependency =
                    this.tableNameHelper.findByApplicationGroupCd(dependencies, tableName)

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

            if (!sets.isEmpty()) {
                this.renameQueryGenerator.generateRenameQuery(bean, dependency)
                this.restoreQueryGenerator.generateRestoreQuery(bean, dependency)
                this.tableDDLGenerator.generateTableDDL(bean, sets, dependency, hashMap)
                this.dropBackupTableQueryGenerator.generateDropBackupTableQuery(bean, dependency)
            }
        }
    }

}
