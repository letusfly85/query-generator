package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndColumnsBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean

/**
 * == IndexDDLGenerator ==
 *
 * @author wada shunsuke
 * @since  2013/12/01
 *
 */
class IndexDDLGenerator {

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

    public String generateDDL(ArrayList<MsIndColumnsBean> list, KrObjectDependenciesBean dependency) {
        initializeQuery()

        return this.query
    }
}
