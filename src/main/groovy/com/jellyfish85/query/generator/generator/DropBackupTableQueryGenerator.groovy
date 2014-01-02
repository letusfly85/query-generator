package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.BaseContext

/**
 * == DropBackupTableQueryGenerator ==
 *
 *
 * @author wada shunsuke
 * @since  2013/12/13
 *
 */
class DropBackupTableQueryGenerator extends  GeneralGenerator {

    public DropBackupTableQueryGenerator(BaseContext _context) {
        super(_context)
    }

    /**
     * == generateDropBackupTableQuery ==
     *
     * @author wada shunsuke
     * @since  2013/12/13
     * @param  dependency
     * @param  msTablesBean
     */
    public void generateDropBackupTableQuery(
            MsTablesBean msTablesBean,
            KrObjectDependenciesBean dependency
            ) {

        this.initializeQuery()

        String tableName   = msTablesBean.physicalTableNameAttr().value()
        String schemaName  = dependency.objectOwnerAttr().value()

        Map map = [
                schemaName  : schemaName,
                tableName   : tableName
        ]

        String path = "/com/jellyfish85/query/generator/template/ddl/dropBackupTableQuery.template"
        this.generate(map, path)
        String dropBackupTableQueryPath =
                this.fileNameHelper.requestDropBackupTableQueryPath(dependency, msTablesBean)
        this.setPath(dropBackupTableQueryPath)
        this.writeAppFile()

    }

}
