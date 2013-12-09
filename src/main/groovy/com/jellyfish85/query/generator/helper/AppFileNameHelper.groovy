package com.jellyfish85.query.generator.helper

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndexesBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.utils.AppProp
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.StringUtils


/**
 * == AppFileNameHelper ==
 *
 * @author wada shunsuke
 * @since  2013/12/05
 *
 */
class AppFileNameHelper {

    private String fileName  = null

    private AppProp prop     = null

    public AppFileNameHelper() {
        prop = new AppProp()
        prop.load()
    }

    /**
     * == requestRestorePath ==
     *
     * @author wada shunsuke
     * @since  2013/12/06
     * @param dependency
     * @param bean
     * @return
     */
    public String indexDDLPath(KrObjectDependenciesBean dependency,
                                     MsIndexesBean bean) {

        String fileName = StringUtils.join(["create_Index_",
                            bean.indexNameAttr().value(),
                            "_",
                            dependency.objectOwnerAttr().value(),
                            ".sql"
                           ], "")

        //todo change restore folder to index folder
        println(this.prop.restoreFolder() + "\t" + fileName)
        String path = FilenameUtils.concat(this.prop.restoreFolder(), fileName)

        return path
    }

    /**
     * == indexDDLPath ==
     *
     * @author wada shunsuke
     * @since  2013/12/09
     * @param dependency
     * @param bean
     * @return
     */
    public String requestRestorePath(KrObjectDependenciesBean dependency,
                                     MsTablesBean bean) {

        String fileName = StringUtils.join(["restore_Table_",
                bean.physicalTableNameAttr().value(),
                "_",
                dependency.objectOwnerAttr().value(),
                ".sql"
        ], "")

        println(this.prop.restoreFolder())
        String path = FilenameUtils.concat(this.prop.restoreFolder(), fileName)

        return path
    }

    /**
     * == sqlLoaderPath ==
     *
     * @author wada shunsuke
     * @since  2013/12/07
     * @param dependency
     * @param bean
     * @return
     */
    public String sqlLoaderPath(KrObjectDependenciesBean dependency,
                                     MsTablesBean bean) {

        String fileName = StringUtils.join(["exec_load.sh"], "")

        println(this.prop.sqlLoaderFolder())
        String path = FilenameUtils.concat(this.prop.sqlLoaderFolder(), fileName)

        return path
    }

}
