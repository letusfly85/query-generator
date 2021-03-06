package com.jellyfish85.query.generator.helper

import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.generator.GeneralGenerator

/**
 * help generate code suite
 *
 *
 */
class CodeGeneratorHelper extends GeneralGenerator {

    public CodeGeneratorHelper(BaseContext _context) {
        super(_context)
    }

    /**
     * generate sql loader script
     *
     * @author wada shunsuke
     * @since  2013/12/30
     * @param tableNames
     * @param shellScriptPath
     */
    public void generateLoadingShellScript(
            HashMap<String, String> tableNames,
            String shellScriptPath
    ) {
        Map map = [
                tableNames  : tableNames
        ]

        String path = "/com/jellyfish85/query/generator/template/shell/execSqlLoader.template"

        this.generate(map, path)
        this.setPath(shellScriptPath)
        this.writeAppFile()
    }

}