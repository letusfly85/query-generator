package com.jellyfish85.query.generator.helper


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
    public String requestRestorePath(KrObjectDependenciesBean dependency,
                                     MsTablesBean bean) {

        String fileName = StringUtils.join(["restore_Table_",
                            bean.physicalTableNameAttr().value(),
                            dependency.objectOwnerAttr().value()
                           ])
        String path = FilenameUtils.concat(this.prop.outputHome(), fileName)

        return path
    }

}
