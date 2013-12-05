package com.jellyfish85.query.generator.helper

/**
 * == ArgumentsHelper ==
 *
 * @author wada shunsuke
 * @since  2013/12/05
 *
 */
class ArgumentsHelper {

    /**
     * == requestTableNameList ==
     *
     * @author wada shunsuke
     * @since  2013/12/05
     * @param tableNames
     * @return
     *
     */
    public ArrayList<String> requestTableNameList(String tableNames) {
        ArrayList<String> list = new ArrayList<String>()

        tableNames.split(/,/).each {String tableName ->
            list.add(tableName)
        }

        return list
    }
}
