package com.jellyfish85.query.generator.generator

import com.jellyfish85.query.generator.BaseContext

/**
 * Generate triggers release DDLs and scripts
 *
 *
 * @author wada shunsuke
 * @since  2013/12/15
 *
 */
class TriggerReleaseScriptsGenerator extends GeneralGenerator {

    private BaseContext context = null
    public TriggerReleaseScriptsGenerator(BaseContext _context) {
        super(_context)
        this.context = super.getBaseContext()
    }

    /**
     * main method
     *
     *
     * @author wada shunsuke
     * @since  2013/12/15
     *
     */
    public void generateTriggerReleaseScripts() {
        //todo

    }

}
