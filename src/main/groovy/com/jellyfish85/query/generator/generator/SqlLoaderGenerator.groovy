package com.jellyfish85.query.generator.generator

import com.jellyfish85.cassandra.accessor.bean.query.generate.tool.CodeInfoBeanTrait
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.helper.TableNameHelper
import groovy.text.SimpleTemplateEngine

class SqlLoaderGenerator<T extends CodeInfoBeanTrait> extends GeneralGenerator {

    private TableNameHelper helper = new TableNameHelper()

    /**
     * == generateDat ==
     *
     *
     * @author wada shunsuke
     * @since  2013/12/07
     * @param list
     * @param dependency
     */
    public void generateControlFile(ArrayList<T> columnList, KrObjectDependenciesBean dependency) {
        this.initializeQuery()

        String tableName   = columnList.head().physicalTableName()
        String schemaName  = dependency.objectOwnerAttr().value()

        SimpleTemplateEngine engine = new SimpleTemplateEngine()

        Map map = [
                schemaName  : schemaName,
                tableName   : tableName,
                columnList  : columnList
        ]

        def path = "/com/jellyfish85/query/generator/template/dml/controlFile.templatetemplate"
        def template = new File(getClass().getResource(path).toURI())

        String query = engine.createTemplate(template).make(map).toString()
        this.setQuery(query)
    }

}
