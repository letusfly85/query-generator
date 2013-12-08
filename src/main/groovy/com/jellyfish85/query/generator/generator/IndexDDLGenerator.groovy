package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndColumnsBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean

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

        String indexName   = columnList.head().indexNameAttr().value()
        String schemaName  = dependency.objectOwnerAttr().value()

        Map map = [
                schemaName  : schemaName,
                tableName   : tableName,
                indexName   : indexName,
                columnList  : columnList
        ]

        String path = "/com/jellyfish85/query/generator/template/dml/indexDDL.template"
        this.generate(map, path)
    }
}
