package com.jellyfish85.query.generator.generator

import com.jellyfish85.query.generator.BaseContext

/**
 * generate stored procedure release ddl and shell scripts
 *
 *
 * @author wada shunsuke
 * @since  2014/01/04
 *
 */
class ProcedureReleaseScriptsGenerator extends GeneralGenerator {

    private BaseContext context = null
    public ProcedureReleaseScriptsGenerator(BaseContext _context) {
        super(_context)
        this.context = super.getBaseContext()
    }

}
