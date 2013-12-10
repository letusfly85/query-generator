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
     * == requestTableDDLPath ==
     *
     * @author wada shunsuke
     * @since  2013/12/10
     * @param dependency
     * @param bean
     * @return
     */
    public String requestTableDDLPath(KrObjectDependenciesBean dependency,
                                      MsTablesBean bean) {

        this.fileName = StringUtils.join(["create_Table_",
                bean.physicalTableNameAttr().value(),
                "_",
                dependency.objectOwnerAttr().value(),
                ".sql"
        ], "")

        println(this.prop.tableDDLFolder() + "\t" + this.fileName)
        String path = FilenameUtils.concat(this.prop.tableDDLFolder(), this.fileName)

        return path
    }

    /**
     * == requestIndexDDLPath ==
     *
     * @author wada shunsuke
     * @since  2013/12/06
     * @param dependency
     * @param bean
     * @return
     */
    public String requestIndexDDLPath(KrObjectDependenciesBean dependency,
                                      MsIndexesBean bean) {

        this.fileName = StringUtils.join(["create_Index_",
                bean.indexNameAttr().value(),
                "_",
                dependency.objectOwnerAttr().value(),
                ".sql"
        ], "")

        println(this.prop.indexDDLFolder() + "\t" + this.fileName)
        String path = FilenameUtils.concat(this.prop.indexDDLFolder(), this.fileName)

        return path
    }

    /**
     * == requestExecuteIndexDDLShellPath ==
     *
     * @author wada shunsuke
     * @since  2013/12/10
     * @param dependency
     * @param bean
     * @return
     */
    public String requestExecuteIndexDDLShellPath() {

        this.fileName = StringUtils.join(["exe_All.sql"
        ], "")

        println(this.prop.indexDDLFolder() + "\t" + this.fileName)
        String path = FilenameUtils.concat(this.prop.indexDDLFolder(), this.fileName)

        return path
    }

    /**
     * == requestRestorePath ==
     *
     * @author wada shunsuke
     * @since  2013/12/09
     * @param dependency
     * @param bean
     * @return
     */
    public String requestRestorePath(KrObjectDependenciesBean dependency,
                                     MsTablesBean bean) {

        this.fileName = StringUtils.join(["restore_Table_",
                bean.physicalTableNameAttr().value(),
                "_",
                dependency.objectOwnerAttr().value(),
                ".sql"
        ], "")

        println(this.prop.restoreFolder())
        String path = FilenameUtils.concat(this.prop.restoreFolder(), this.fileName)

        return path
    }

    /**
     * == requestSqlLoaderPath ==
     *
     * @author wada shunsuke
     * @since  2013/12/07
     * @param dependency
     * @param bean
     * @return
     */
    public String requestSqlLoaderPath(KrObjectDependenciesBean dependency,
                                     MsTablesBean bean) {

        this.fileName = StringUtils.join(["exec_load.sh"], "")

        println(this.prop.sqlLoaderFolder())
        String path = FilenameUtils.concat(this.prop.sqlLoaderFolder(), this.fileName)

        return path
    }
}
