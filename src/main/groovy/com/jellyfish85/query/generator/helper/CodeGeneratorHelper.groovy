package com.jellyfish85.query.generator.helper

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.query.generator.generator.GeneralGenerator

/**
 * help generate code suite
 *
 *
 */
class CodeGeneratorHelper extends GeneralGenerator {

    /**
     * generate sql loader script
     *
     * @author wada shunsuke
     * @since  2013/12/30
     * @param schemaName
     * @param tablesBeans
     * @param shellScriptPath
     */
    public void generateLoadingShellScript(
            String schemaName,
            ArrayList<MsTablesBean> tablesBeans,
            String shellScriptPath
    ) {
        String tableName = tablesBeans.head().physicalTableNameAttr().value()

        Map map = [
                schemaName  : schemaName,
                tableName   : tableName
        ]

        String path = "/com/jellyfish85/query/generator/template/shell/execSqlLoader.template"

        this.generate(map, path)
        this.setPath(shellScriptPath)
        this.writeAppFile()
    }

}
