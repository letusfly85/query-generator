package com.jellyfish85.query.generator.helper

/**
 * == BINameHelper ==
 *
 * @author wada shunsuke
 * @since  2013/12/02
 *
 */
class BINameHelper {

    private String biTableName = null

    /**
     * == requestBITableName ==
     *
     * @author wada shunsuke
     * @since  2013/12/02
     * @param tableName
     * @param option
     * @return
     */
    public String requestBITableName(String tableName) {
        return requestBITableName(tableName, "")
    }

    /**
     * == requestBITableName ==
     *
     * @author wada shunsuke
     * @since  2013/12/02
     * @param tableName
     * @param option
     * @return
     */
    public String requestBITableName(String tableName, String option) {
        if (tableName.startsWith("M")) {
            this.biTableName = "C" + tableName

        } else if (tableName.startsWith("T")) {
            this.biTableName = "R" + tableName
        }

        return this.biTableName
    }

}
