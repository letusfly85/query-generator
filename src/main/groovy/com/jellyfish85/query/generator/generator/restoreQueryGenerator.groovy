package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.BaseContext

/**
 * == RestoreQueryGenerator ==
 *
 * @author wada shunsuke
 * @since  2013/12/02
 *
 */
class RestoreQueryGenerator extends GeneralGenerator {

    private BaseContext context = null
    public RestoreQueryGenerator(BaseContext _context) {
        super(_context)
        this.context = super.getBaseContext()
    }

    /**
     * == generateRestoreQuery ==
     *
     * @author wada shunsuke
     * @since  2013/12/03
     * @param list
     * @return
     *
     */
   public void generateRestoreQuery(
           MsTablesBean                 msTablesBean,
           KrObjectDependenciesBean     dependency) {

       String tableName   = msTablesBean.physicalTableNameAttr().value()
       String bkTableName = this.context.tableNameHelper.requestBKTableName(tableName)
       String schemaName  = dependency.objectOwnerAttr().value()

       Map map = [
               schemaName  : schemaName,
               tableName   : tableName,
               bkTableName : bkTableName
       ]

       String path = "/com/jellyfish85/query/generator/template/dml/restoreTable.template"

       this.generate(map, path)
       String restoreQueryPath =
               this.context.fileNameHelper.requestRestorePath(dependency, msTablesBean)
       this.setPath(restoreQueryPath)
       this.writeAppFile()
   }

}