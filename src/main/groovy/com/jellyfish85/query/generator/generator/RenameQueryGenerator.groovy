package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.BaseContext

/**
 * == RenameQueryGenerator ==
 *
 * @author wada shunsuke
 * @since  2013/12/13
 *
 */
class RenameQueryGenerator extends GeneralGenerator {

    public RenameQueryGenerator(BaseContext _context) {
        super(_context)
    }

    /**
     * == generateRenameQuery ==
     *
     *
     * @author wada shunsuke
     * @since  2013/12/13
     *
     */
    public void generateRenameQuery(
            MsTablesBean             msTablesBean,
            KrObjectDependenciesBean dependency
            ) {

        this.initializeQuery()

        String tableName   = msTablesBean.physicalTableNameAttr().value()
        String schemaName  = dependency.objectOwnerAttr().value()

        Map map = [
                schemaName  : schemaName,
                tableName   : tableName
        ]

        String path = "/com/jellyfish85/query/generator/template/ddl/renameQuery.template"
        this.generate(map, path)
        String renameQueryPath =
                this.context.fileNameHelper.requestRenamePath(dependency, msTablesBean)
        this.setPath(renameQueryPath)
        this.writeAppFile()
    }

}
