package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.query.generator.helper.BINameHelper
import groovy.text.SimpleTemplateEngine

/**
 * == restoreQueryGenerator ==
 *
 * @author wada shunsuke
 * @since  2013/12/02
 *
 */
class restoreQueryGenerator extends GeneralGenerator {

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
   public String generate(ArrayList<MsTabColumnsBean> list) {
       this.initializeQuery()

       BINameHelper helper = new BINameHelper()

       String tableName   = list.head().physicalTableNameAttr().value()
       String biTableName = helper.requestBITableName(tableName)

       SimpleTemplateEngine engine = new SimpleTemplateEngine()

       Map map = [
               tableName   : tableName,
               biTableName : biTableName,
               columnList  : list
       ]

       def path = "/com/jellyfish85/query/generator/template/dml/restoreTable.template"
       def template = new File(getClass().getResource(path).toURI())

       setQuery(engine.createTemplate(template).make(map).toString())
       String query = getQuery()

       return query
   }

}