package com.jellyfish85.query.generator.generator

/**
 * == RenameQueryGenerator ==
 *
 * @author wada shunsuke
 * @since  2013/12/13
 *
 */
class RenameQueryGenerator extends GeneralGenerator {

    /**
     *
     * @todo
     */
    public void generateRenameQuery() {
        this.initializeQuery()

        String query = "restore"

        this.setQuery(query)
    }

}
