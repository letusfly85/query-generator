package com.jellyfish85.query.generator.helper

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsIndexesBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.utils.QueryAppProp
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

    private String fileName       = null

    private QueryAppProp queryProp = null

    public AppFileNameHelper(QueryAppProp _queryProp) {
        this.queryProp = _queryProp
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

        String path = FilenameUtils.concat(this.queryProp.tableDDLFolder(), this.fileName)

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

        println(this.queryProp.indexDDLFolder() + "\t" + this.fileName)
        String path = FilenameUtils.concat(this.queryProp.indexDDLFolder(), this.fileName)

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

        println(this.queryProp.indexDDLFolder() + "\t" + this.fileName)
        String path = FilenameUtils.concat(this.queryProp.indexDDLFolder(), this.fileName)

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

        String path = FilenameUtils.concat(this.queryProp.tableDDLFolder(), this.fileName)

        return path
    }

    /**
     * == requestRenamePath ==
     *
     * @author wada shunsuke
     * @since  2013/12/13
     * @param dependency
     * @param bean
     * @return
     */
    public String requestRenamePath(KrObjectDependenciesBean dependency,
                                    MsTablesBean bean) {

        this.fileName = StringUtils.join(["rename_Table_",
                bean.physicalTableNameAttr().value(),
                "_",
                dependency.objectOwnerAttr().value(),
                ".sql"
        ], "")

        println(this.queryProp.restoreFolder())
        String path = FilenameUtils.concat(this.queryProp.tableDDLFolder(), this.fileName)

        return path
    }

    /**
     * == requestDropBackupTableQueryPath ==
     *
     * @author wada shunsuke
     * @since  2013/12/13
     * @param dependency
     * @param bean
     * @return
     */
    public String requestDropBackupTableQueryPath(
            KrObjectDependenciesBean dependency,
            MsTablesBean bean) {

        this.fileName = StringUtils.join(["drop_BackupTable_",
                bean.physicalTableNameAttr().value(),
                "_",
                dependency.objectOwnerAttr().value(),
                ".sql"
        ], "")

        println(this.queryProp.restoreFolder())
        String path = FilenameUtils.concat(this.queryProp.tableDDLFolder(), this.fileName)

        return path
    }

    /**
     * == requestExecuteQueriesPath ==
     *
     * @author wada shunsuke
     * @since  2013/12/13
     * @param dependency
     * @param bean
     * @return
     */
    public String requestExecuteQueriesPath(
            KrObjectDependenciesBean dependency,
            MsTablesBean bean) {

        this.fileName = StringUtils.join(["exec_",
                bean.physicalTableNameAttr().value(),
                "_",
                dependency.objectOwnerAttr().value(),
                ".sql"
        ], "")

        println(this.queryProp.restoreFolder())
        String path = FilenameUtils.concat(this.queryProp.tableDDLFolder(), this.fileName)

        return path
    }

    /**
     * == requestExecuteAllQueriesPath ==
     *
     * @author wada shunsuke
     * @since  2013/12/13
     * @return
     *
     */
    public String requestExecuteAllQueriesPath() {

        this.fileName = StringUtils.join(["exe_All.sql"
        ], "")

        println(this.queryProp.restoreFolder())
        String path = FilenameUtils.concat(this.queryProp.tableDDLFolder(), this.fileName)

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

        println(this.queryProp.sqlLoaderParentPath())
        String path = FilenameUtils.concat(this.queryProp.sqlLoaderParentPath(), this.fileName)

        return path
    }

    /**
     * request general code loading shell script path
     *
     * @author wada shunsuke
     * @since  2014/01/01
     * @param dependency
     * @param bean
     * @return
     */
    public String requestSqlLoaderPath4GeneralCode() {
        this.fileName = StringUtils.join(["exec_load4general_code.sh"], "")

        println(this.queryProp.sqlLoaderParentPath())
        String path = FilenameUtils.concat(this.queryProp.sqlLoaderParentPath(), this.fileName)

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
    public String requestSqlLoaderPath4UniqueCode() {
        this.fileName = StringUtils.join(["exec_load4unique_code.sh"], "")

        println(this.queryProp.sqlLoaderParentPath())
        String path = FilenameUtils.concat(this.queryProp.sqlLoaderParentPath(), this.fileName)

        return path
    }

    /**
     * == requestSqlLoaderPath4TemplateRecord ==
     *
     * @author wada shunsuke
     * @since  2014/01/01
     * @param dependency
     * @param bean
     * @return
     */
    public String requestSqlLoaderPath4TemplateRecord() {
        this.fileName = StringUtils.join(["exec_load4template_record.sh"], "")

        println(this.queryProp.sqlLoaderParentPath())
        String path = FilenameUtils.concat(this.queryProp.sqlLoaderParentPath(), this.fileName)

        return path
    }

    /**
     * request sql loader script for authority code
     *
     * @author wada shunsuke
     * @since  2014/01/02
     * @param dependency
     * @param bean
     * @return
     */
    public String requestSqlLoaderPath4AuthorityCode() {
        this.fileName = StringUtils.join(["exec_load4authority_code.sh"], "")

        println(this.queryProp.sqlLoaderParentPath())
        String path = FilenameUtils.concat(this.queryProp.sqlLoaderParentPath(), this.fileName)

        return path
    }


    /**
     * request sql loader script for error check code
     *
     * @author wada shunsuke
     * @since  2014/01/02
     * @param dependency
     * @param bean
     * @return
     */
    public String requestSqlLoaderPath4ErrorCheckCode() {
        this.fileName = StringUtils.join(["exec_load4error_check_code.sh"], "")

        println(this.queryProp.sqlLoaderParentPath())
        String path = FilenameUtils.concat(this.queryProp.sqlLoaderParentPath(), this.fileName)

        return path
    }

    /**
     * return sql loader dat file path
     *
     * @author wada shunsuke
     * @since  2013/12/07
     * @param dependency
     * @param bean
     * @return
     */
    public String requestSqlLoaderDataPath(MsTablesBean bean) {

        this.fileName = StringUtils.join([
                    bean.physicalTableNameAttr().value(),
                    ".dat"
                ], "")

        String path = FilenameUtils.concat(this.queryProp.sqlLoaderDatPath(), this.fileName)

        return path
    }

    /**
     * return sql loader control file path
     *
     * @author wada shunsuke
     * @since  2013/12/23
     * @param dependency
     * @param tableName
     * @return
     */
    public String requestSqlLoaderControlPath(String tableName) {

        this.fileName = StringUtils.join([
                tableName,
                ".ctl"
        ], "")

        String path = FilenameUtils.concat(this.queryProp.sqlLoaderCtlPath(), this.fileName)

        return path
    }

    public String requestSqlLoaderDeleteTablePath(String schemaName, String tableName) {
        this.fileName = StringUtils.join([
                "DELETE_",
                tableName,
                "_",
                schemaName,
                ".sql"
        ], "")

        String path = FilenameUtils.concat(this.queryProp.sqlLoaderParentPath(), this.fileName)

        return path
    }
}
