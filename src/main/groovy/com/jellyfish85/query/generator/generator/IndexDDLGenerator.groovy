package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndexesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsIndColumnsDao
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsIndexesDao
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.query.generator.helper.AppFileNameHelper
import com.jellyfish85.query.generator.helper.TableNameHelper
import com.jellyfish85.query.generator.runner.BaseRunner

import java.sql.Connection

/**
 * == IndexDDLGenerator ==
 *
 * @author wada shunsuke
 * @since  2013/12/01
 *
 */
class IndexDDLGenerator extends GeneralGenerator {

    public generateIndexDDL(String tableName,
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

        String path = "/com/jellyfish85/query/generator/template/ddl/indexDDL.template"
        this.generate(map, path)
    }

    /**
     * == generateIndexDDL ==
     *
     * @author wada shunsuke
     * @since  2013/12/09
     * @param conn
     * @param fileNameHelper
     * @param dependencyGrpCd
     */
    public void generateIndexDDL(
             Connection conn,
             AppFileNameHelper fileNameHelper,
             String dependencyGrpCd) {

        // generate dao instances
        KrObjectDependenciesDao krObjectDependenciesDao =
                new KrObjectDependenciesDao()
        MsIndexesDao    msIndexesDao    = new MsIndexesDao()
        MsIndColumnsDao msIndColumnsDao = new MsIndColumnsDao()

        // specify dependencies
        def _dependencies = krObjectDependenciesDao.findByDependencyGrpCd(conn, dependencyGrpCd)
        ArrayList<KrObjectDependenciesBean> dependencies = krObjectDependenciesDao.convert(_dependencies)

        def _list = msIndexesDao.findAll(conn)
        ArrayList<MsIndexesBean> list = msIndexesDao.convert(_list)

        // generate helper
        TableNameHelper tableNameHelper  = new TableNameHelper()

        // generate query each by table
        list.each {MsIndexesBean bean ->
            String tableName = bean.physicalTableNameAttr().value()
            KrObjectDependenciesBean dependency =
                    tableNameHelper.findByApplicationGroupCd(dependencies, tableName)

            def _sets = msIndColumnsDao.find(conn, bean)
            ArrayList<MsIndColumnsBean> sets = msIndColumnsDao.convert(_sets)

            if (!sets.isEmpty()) {
                this.generateIndexDDL(tableName, sets, dependency)

                String indexDDLPath =
                        fileNameHelper.indexDDLPath(dependency, bean)
                this.setPath(indexDDLPath)

                this.writeAppFile()
            }
        }
    }
}
