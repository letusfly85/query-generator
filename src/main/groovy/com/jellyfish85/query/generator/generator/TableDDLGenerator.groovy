package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTabColumnsDao
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.query.generator.helper.AppFileNameHelper
import com.jellyfish85.query.generator.helper.TableNameHelper
import org.apache.commons.lang.ArrayUtils
import org.apache.commons.lang.math.NumberUtils

import java.sql.Connection
import java.text.DecimalFormat

/**
 * == TableDDLGenerator ==
 *
 * @author wada shunsuke
 * @since  2013/12/01
 *
 */
class TableDDLGenerator extends GeneralGenerator {

    // generate helper
    private TableNameHelper tableNameHelper  = new TableNameHelper()

    /**
     * == generateDDL ==
     *
     * @author wada shunsuke
     * @since  2013/12/01
     * @param bean
     * @param columnList
     * @param dependency
     *
     */
    public void generateTableDDL(
            MsTablesBean                bean,
            ArrayList<MsTabColumnsBean> columnList,
            KrObjectDependenciesBean dependency) {

        this.initializeQuery()

        String tableName   = columnList.head().physicalTableNameAttr().value()
        String schemaName  = dependency.objectOwnerAttr().value()
        String uniqueTag   = generateUniqueTag(bean)

        ArrayList<MsTabColumnsBean> pkList = new ArrayList<MsTabColumnsBean>()
        for (column in columnList) {
            if (NumberUtils.isNumber(column.pkFlgAttr().value())) {
                pkList.add(column)
            }
        }


        //todo index ddl
        //todo table version

        Map map = [
                schemaName  : schemaName,
                tableName   : tableName,
                uniqueTag   : uniqueTag,
                columnList  : columnList,
                pkList      : pkList
        ]

        String path = "/com/jellyfish85/query/generator/template/ddl/tableDDL.template"
        this.generate(map, path)
    }

    /**
     *
     *
     *
     * @param bean
     * @return
     */
    public String generateUniqueTag(MsTablesBean bean) {
        DecimalFormat df = new DecimalFormat("00000")
        String tableId = df.format(bean.tableIdAttr().value())

        df = new DecimalFormat("000000000")
        String tableRevision = df.format(bean.revisionAttr().value())
        String comment = "[" + tableId + "][" + tableRevision + "]"

        return comment
    }

    /**
     * == generateTableDDL ==
     *
     * @author wada shunsuke
     * @since  2013/12/09
     * @param conn
     * @param fileNameHelper
     * @param dependencyGrpCd
     */
    public void generateTableDDL(
            Connection conn,
            AppFileNameHelper fileNameHelper,
            String dependencyGrpCd,
            ArrayList<MsTablesBean> tableList
    ) {

        // generate dao instances
        KrObjectDependenciesDao krObjectDependenciesDao =
                new KrObjectDependenciesDao()
        MsTabColumnsDao msTabColumnsDao = new MsTabColumnsDao()

        // specify dependencies
        def _dependencies = krObjectDependenciesDao.findByDependencyGrpCd(conn, dependencyGrpCd)
        ArrayList<KrObjectDependenciesBean> dependencies = krObjectDependenciesDao.convert(_dependencies)

        // generate query each by table
        tableList.each {MsTablesBean bean ->
            String tableName = bean.physicalTableNameAttr().value()
            KrObjectDependenciesBean dependency =
                    this.tableNameHelper.findByApplicationGroupCd(dependencies, tableName)

            def _sets = msTabColumnsDao.find(conn, bean)
            ArrayList<MsTabColumnsDao> sets = msTabColumnsDao.convert(_sets)

            if (!sets.isEmpty()) {
                this.generateTableDDL(bean, sets, dependency)

                String tableDDLPath =
                        fileNameHelper.requestTableDDLPath(dependency, bean)
                this.setPath(tableDDLPath)

                this.writeAppFile()
            }
        }

        //todo
        //generateExecuteIndexDDLShell(fileNameHelper, dependencies, list)
    }

}
