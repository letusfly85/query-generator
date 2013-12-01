package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean

/**
 * == TableDDLGenerator ==
 *
 * @author wada shunsuke
 * @since  2013/12/01
 *
 */
class TableDDLGenerator {

    private String query = null

    /**
     * == initializeQuery ==
     *
     * @author wada shunsuke
     * @since  2013/12/01
     *
     */
    protected void initializeQuery() {
        this.query = null
    }

    /**
     * == generateDDL ==
     *
     * @author wada shunsuke
     * @since  2013/12/01
     * @param list
     * @param dependency
     * @return
     */
    public String generateDDL(ArrayList<MsTabColumnsBean> list, KrObjectDependenciesBean dependency) {
        initializeQuery()

        return query
    }

    /**
     * == generateTableDDL ==
     *
     * @author wada shunsuke
     * @since  2013/12/01
     * @param list
     * @param dependency
     * @return
     */
    public String generateTableDDL(ArrayList<MsTabColumnsBean> list, KrObjectDependenciesBean dependency) {
        initializeQuery()

        return query
    }

    /**
     * == generatePKDDL ==
     *
     * @author wada shunsuke
     * @since  2013/12/01
     * @param list
     * @param dependency
     * @return
     */
    public String generatePKDDL(ArrayList<MsTabColumnsBean> list, KrObjectDependenciesBean dependency) {
        initializeQuery()

        return query
    }

    /**
     * == generateComments ==
     *
     * @author wada shunsuke
     * @since  2013/12/01
     * @param list
     * @param dependency
     * @return
     */
    public String generateComments(ArrayList<MsTabColumnsBean> list, KrObjectDependenciesBean dependency) {
        initializeQuery()

        return query
    }

}
