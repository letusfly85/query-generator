package com.jellyfish85.query.generator.generator

/**
 * == GeneralGenerator ==
 *
 * @author wada shunsuke
 * @since  2013/12/02
 *
 */
class GeneralGenerator {

    private String query = null

    /**
     * == initializeQuery ==
     *
     * @author wada shunsuke
     * @since  2013/12/03
     *
     */
    protected void initializeQuery() {
        this.query = null
    }

    public void setQuery(String _query) {
        this.query = _query
    }

    public String getQuery() {
        return this.query
    }

}
