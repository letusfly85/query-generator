package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndexesBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import org.apache.commons.lang.math.NumberUtils

import java.text.DecimalFormat

/**
 * == TableDDLGenerator ==
 *
 * @author wada shunsuke
 * @since  2013/12/01
 *
 */
class TableDDLGenerator extends GeneralGenerator {

    /**
     * == generateDDL ==
     *
     * @author wada shunsuke
     * @since  2013/12/01
     * @param msTablesBean
     * @param columnList
     * @param dependency
     *
     */
    public void generateTableDDL(
            MsTablesBean                msTablesBean,
            ArrayList<MsTabColumnsBean> columnList,
            KrObjectDependenciesBean dependency,
            HashMap<MsIndexesBean, ArrayList<MsIndColumnsBean>> hashMap) {

        this.initializeQuery()

        String tableName   = columnList.head().physicalTableNameAttr().value()
        String schemaName  = dependency.objectOwnerAttr().value()
        String uniqueTag   = generateUniqueTag(msTablesBean)

        ArrayList<MsTabColumnsBean> pkList = new ArrayList<MsTabColumnsBean>()
        for (column in columnList) {
            if (NumberUtils.isNumber(column.pkFlgAttr().value())) {
                pkList.add(column)
            }
        }

        Map map = [
                schemaName  : schemaName,
                tableName   : tableName,
                uniqueTag   : uniqueTag,
                tableBean   : msTablesBean,
                columnList  : columnList,
                indHashMap  : hashMap,
                pkList      : pkList
        ]

        String path = "/com/jellyfish85/query/generator/template/ddl/tableDDL.template"

        this.generate(map, path)
        String tableDDLPath =
                this.fileNameHelper.requestTableDDLPath(dependency, msTablesBean)
        this.setPath(tableDDLPath)
        this.writeAppFile()
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
}
