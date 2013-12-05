package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTabColumnsDao
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTablesDao
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.query.generator.helper.AppFileNameHelper
import com.jellyfish85.query.generator.helper.TableNameHelper

import groovy.text.SimpleTemplateEngine
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
     * == generate ==
     *
     * @author wada shunsuke
     * @since  2013/12/03
     * @param list
     * @return
     *
     * @todo fix to dba_ins_timestamp and so on
     */
   public void generate(ArrayList<MsTabColumnsBean> list, KrObjectDependenciesBean dependency) {
       this.initializeQuery()

       String tableName   = list.head().physicalTableNameAttr().value()
       String bkTableName = this.helper.requestBKTableName(tableName)
       String schemaName  = dependency.objectOwnerAttr().value()

       SimpleTemplateEngine engine = new SimpleTemplateEngine()

       Map map = [
               schemaName  : schemaName,
               tableName   : tableName,
               bkTableName : bkTableName
       ]

       def path = "/com/jellyfish85/query/generator/template/dml/restoreTable.template"
       def template = new File(getClass().getResource(path).toURI())

       String query = engine.createTemplate(template).make(map).toString()
       this.setQuery(query)
   }

    /**
     * == generate ==
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

        // generate dao instances
        KrObjectDependenciesDao  krObjectDependenciesDao =
                new KrObjectDependenciesDao()
        MsTablesDao msTablesDao         = new MsTablesDao()
        MsTabColumnsDao msTabColumnsDao = new MsTabColumnsDao()

        // get dependency sets
        def _dependencySets =
                krObjectDependenciesDao.findByDependencyGrpCd(conn, dependencyGrpCd)
        ArrayList<KrObjectDependenciesBean> dependencySets =
                krObjectDependenciesDao.convert(_dependencySets)

        // generate queries by each table
        list.each {String tableName ->
            KrObjectDependenciesBean dependency =
                    helper.findByApplicationGroupCd(dependencySets, tableName)

            MsTablesBean msTablesBean = new MsTablesBean()
            msTablesBean.physicalTableNameAttr().setValue(tableName)
            msTablesBean =  msTablesDao.findOne(conn, msTablesBean)

            def _sets = msTabColumnsDao.find(conn, msTablesBean)
            ArrayList<MsTabColumnsBean> sets = msTabColumnsDao.convert(_sets)

            generate(sets, dependency)

            this.setPath(fileNameHelper.requestRestorePath(dependency, msTablesBean))
            this.writeAppFile()
        }
    }
}