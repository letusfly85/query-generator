package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTabColumnsDao
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTablesDao
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.query.generator.helper.AppFileNameHelper
import com.jellyfish85.query.generator.helper.TableNameHelper

import java.sql.Connection

/**
 * == RestoreQueryGenerator ==
 *
 * @author wada shunsuke
 * @since  2013/12/02
 *
 */
class RestoreQueryGenerator extends GeneralGenerator {

    private TableNameHelper helper = new TableNameHelper()

    /**
     * == generateRestoreQuery ==
     *
     * @author wada shunsuke
     * @since  2013/12/03
     * @param list
     * @return
     *
     */
   public void generateRestoreQuery(ArrayList<MsTabColumnsBean> list, KrObjectDependenciesBean dependency) {

       String tableName   = list.head().physicalTableNameAttr().value()
       String bkTableName = this.helper.requestBKTableName(tableName)
       String schemaName  = dependency.objectOwnerAttr().value()

       Map map = [
               schemaName  : schemaName,
               tableName   : tableName,
               bkTableName : bkTableName
       ]

       String path = "/com/jellyfish85/query/generator/template/dml/restoreTable.template"

       this.generate(map, path)
   }

    /**
     * == generateRestoreQuery ==
     *
     * @author wada shunsuke
     * @since  2013/12/05
     * @param list
     * @return
     *
     */
    public void generate(
            Connection conn,
            AppFileNameHelper fileNameHelper,
            String dependencyGrpCd,
            ArrayList<String> list
    ) {

        // generateRestoreQuery dao instances
        KrObjectDependenciesDao  krObjectDependenciesDao =
                new KrObjectDependenciesDao()
        MsTablesDao msTablesDao         = new MsTablesDao()
        MsTabColumnsDao msTabColumnsDao = new MsTabColumnsDao()

        // get dependency sets
        def _dependencySets =
                krObjectDependenciesDao.findByDependencyGrpCd(conn, dependencyGrpCd)
        ArrayList<KrObjectDependenciesBean> dependencySets =
                krObjectDependenciesDao.convert(_dependencySets)

        // generateRestoreQuery queries by each table
        list.each {String tableName ->
            KrObjectDependenciesBean dependency =
                    helper.findByApplicationGroupCd(dependencySets, tableName)

            MsTablesBean msTablesBean = new MsTablesBean()
            msTablesBean.physicalTableNameAttr().setValue(tableName)
            msTablesBean =  msTablesDao.findOne(conn, msTablesBean)

            def _sets = msTabColumnsDao.find(conn, msTablesBean)
            ArrayList<MsTabColumnsBean> sets = msTabColumnsDao.convert(_sets)

            generateRestoreQuery(sets, dependency)

            String restoreQueryPath =
                    fileNameHelper.requestRestorePath(dependency, msTablesBean)
            this.setPath(restoreQueryPath)
            this.writeAppFile()
        }
    }
}