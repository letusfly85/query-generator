package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.helper.TableNameHelper
import groovy.text.SimpleTemplateEngine

/**
 * == RestoreQueryGenerator ==
 *
 * @author wada shunsuke
 * @since  2013/12/02
 *
 */
class RestoreQueryGenerator extends GeneralGenerator {

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
   public String generate(ArrayList<MsTabColumnsBean> list, KrObjectDependenciesBean dependency) {
       this.initializeQuery()

       TableNameHelper helper = new TableNameHelper()

       String tableName   = list.head().physicalTableNameAttr().value()
       String bkTableName = helper.requestBKTableName(tableName)
       String schemaName  = dependency.objectOwnerAttr().value()

       SimpleTemplateEngine engine = new SimpleTemplateEngine()

       Map map = [
               schemaName  : schemaName,
               tableName   : tableName,
               bkTableName : bkTableName
       ]

       def path = "/com/jellyfish85/query/generator/template/dml/restoreTable.template"
       def template = new File(getClass().getResource(path).toURI())

       setQuery(engine.createTemplate(template).make(map).toString())
       String query = getQuery()

       return query
   }

}