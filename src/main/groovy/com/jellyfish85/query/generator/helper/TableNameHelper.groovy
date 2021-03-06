package com.jellyfish85.query.generator.helper

import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.constant.QueryAppConst
import com.jellyfish85.query.generator.utils.QueryAppProp
import org.apache.commons.lang.StringUtils

/**
 * == TableNameHelper ==
 *
 * @author wada shunsuke
 * @since  2013/12/02
 *
 */
class TableNameHelper {

    private String biTableName = null
    private String bkTableName = null

    private QueryAppProp queryProp = null
    public TableNameHelper(QueryAppProp _queryProp) {
        this.queryProp = _queryProp
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
            this.biTableName = "C" + StringUtils.substring(tableName, 1)

        } else if (tableName.startsWith("T")) {
            this.biTableName = "H" + StringUtils.substring(tableName, 1)
        }

        return this.biTableName
    }

    /**
     * == requestBKTableName ==
     *
     * @author wada shunsuke
     * @since  2013/12/03
     * @param tableName
     * @param option
     * @return
     */
    public String requestBKTableName(String tableName) {
        return requestBKTableName(tableName, "")
    }

    /**
     * == requestBKTableName ==
     *
     * @author wada shunsuke
     * @since  2013/12/03
     * @param tableName
     * @param option
     * @return
     */
    public String requestBKTableName(String tableName, String option) {
        this.bkTableName = "BK_" + tableName

        return this.bkTableName
    }

    /**
     * == identifyTableGroup ==
     *
     * @author wada shunsuke
     * @since  2013/12/03
     * @param  tableName
     * @return
     */
    public String identifyTableGroup(String tableName) {

        if (tableName.startsWith("WR_IK")) {
            return QueryAppConst.APPLICATION_GROUP_IK

        } else if (tableName.startsWith("WI_IK_")) {
            return QueryAppConst.APPLICATION_GROUP_IK

        } else if (tableName.startsWith("M_IK_")) {
            return QueryAppConst.APPLICATION_GROUP_IK

        } else if (tableName.startsWith("WR")) {
            return QueryAppConst.APPLICATION_GROUP_IF

        } else if (tableName.startsWith("WS")) {
            return QueryAppConst.APPLICATION_GROUP_IF

        } else if (tableName.startsWith("WI")) {
            return QueryAppConst.APPLICATION_GROUP_APP

        } else if (tableName.startsWith("E_J")) {
            return QueryAppConst.APPLICATION_GROUP_APP

        } else if (tableName.startsWith("E_K")) {
            return QueryAppConst.APPLICATION_GROUP_APP

        } else if (tableName.startsWith("E_IK")) {
            return QueryAppConst.APPLICATION_GROUP_APP

        } else if (isIkTable(tableName)) {
            return QueryAppConst.APPLICATION_GROUP_IK

        } else if (tableName.startsWith("WH_")) {
            return QueryAppConst.APPLICATION_GROUP_BI

        } else if (tableName.startsWith("H_")) {
            return QueryAppConst.APPLICATION_GROUP_BI

        } else if (tableName.startsWith("C_")) {
            return QueryAppConst.APPLICATION_GROUP_BI

        } else {
            return QueryAppConst.APPLICATION_GROUP_APP
        }
    }

    /**
     * identify service name
     *
     * @author wada shunsuke
     * @since  2014/03/19
     * @param  tableName
     * @return
     */
    public String identifyServiceName(String tableName) {

        if (tableName.startsWith("WR_IK")) {
            return queryProp.erdServiceNameMain()

        } else if (tableName.startsWith("WI_IK_")) {
            return queryProp.erdServiceNameMain()

        } else if (tableName.startsWith("M_IK_")) {
            return queryProp.erdServiceNameMain()

        } else if (tableName.startsWith("WR")) {
            return queryProp.erdServiceNameMain()

        } else if (tableName.startsWith("WS")) {
            return queryProp.erdServiceNameMain()

        } else if (tableName.startsWith("WI")) {
            return queryProp.erdServiceNameMain()

        } else if (tableName.startsWith("E_J")) {
            return queryProp.erdServiceNameMain()

        } else if (tableName.startsWith("E_K")) {
            return queryProp.erdServiceNameMain()

        } else if (tableName.startsWith("E_IK")) {
            return queryProp.erdServiceNameMain()

        } else if (isIkTable(tableName)) {
            return queryProp.erdServiceNameMain()

        } else if (tableName.startsWith("WH_")) {
            return queryProp.erdServiceNameBI()

        } else if (tableName.startsWith("H_")) {
            return queryProp.erdServiceNameBI()

        } else if (tableName.startsWith("C_")) {
            return queryProp.erdServiceNameBI()

        } else {
            return queryProp.erdServiceNameMain()
        }
    }

    /**
     *
     *
     * @param tableName
     * @return
     */
    public Boolean isBITable(String tableName) {
        if (identifyTableGroup(tableName).equals(QueryAppConst.APPLICATION_GROUP_BI)) {
            return true

        } else {
            return false
        }
    }

    /**
     * return revert bi table name
     *
     *
     * @param tableName
     * @return
     */
    public String getRevertName(String tableName) {
        String revertTableName = ""
        if (tableName.startsWith("WH_")) {
            revertTableName = tableName.replaceFirst("WH_", "RWH_")

        } else if (tableName.startsWith("H_")) {
            revertTableName = tableName.replaceFirst("H_", "RH_")

        } else if (tableName.startsWith("C_")) {
            revertTableName = tableName.replaceFirst("C_", "RC_")
        }

        return revertTableName
    }

  /**
     * == isIkTable ==
     *
     *
     * @author wada shunsuke
     * @since  2013/12/03
     * @param  tableName
     * @return
     *
     * @todo hide from source code
     */
    public Boolean isIkTable(String tableName) {
        Boolean result = false

        ArrayList<String> exceptionList = [
                "KNRKKINF_1",
                "ANKBKSRK",
                "ANKNINF",
                "MMINF",
                "TKYKNMS",
                "TKKRKNMS",
                "HJNINF",
                "SKKTKYK",
                "SKKTKTK",
                "SKKTKZK",
                "SKKTKSS",
                "SKKTKTR",
                "SKKTKJG",
                "SKKTKGS",
                "SKKTKKG",
                "KSYRRK",
                "KKKHNINF_1",
                "KNKSINF"
        ]

        exceptionList.each {String eTable ->
            if (eTable == tableName) {
                result = true
            }
        }

        return result
    }

    /**
     * == consistencyCheckExists ==
     *
     *
     * @author wada shunsuke
     * @since  2013/12/03
     * @param  tableName
     * @return
     */
    public Boolean consistencyCheckExists(String tableName) {
        Boolean result = false

        if (tableName.startsWith("WR_IK")) {
            result =  false

        } else if (tableName.startsWith("WI_IK_")) {
            result =  false

        } else if (tableName.startsWith("H_")) {
            result =  false

        } else if (tableName.startsWith("WR")) {
            result =  false

        } else if (tableName.startsWith("WS")) {
            result =  false

        } else if (tableName.startsWith("WI")) {
            result =  false

        } else if (isIkTable(tableName)) {
            result =  false

        } else if (tableName.startsWith("E_")) {
            result =  false

        } else {
            result =  true
        }

        return result
    }

    /**
     * == KrObjectDependenciesBean ==
     *
     *
     * @author  wada shunsuke
     * @since   2013/12/03
     * @param   list
     * @param   applicationGroupCd
     * @return
     */
    public KrObjectDependenciesBean findByApplicationGroupCd(
            ArrayList<KrObjectDependenciesBean> list,
            String tableName
    ) {
        KrObjectDependenciesBean result = null
        String applicationGroupCd = identifyTableGroup(tableName)

        list.each {KrObjectDependenciesBean bean ->
            if (bean.ifFlgAttr().value() == applicationGroupCd) {
                result =  bean
            }
        }

        return result
    }

    /**
     * request schema name by dependency group
     *
     *
     * @param dependencyGrpCd
     * @return
     */
    public requestMainSchemaName(String dependencyGrpCd) {
        if (dependencyGrpCd.equals(QueryAppConst.APPLICATION_MAIN_GROUP)) {
            return queryProp.erdSchemaMainName()

        } else if (dependencyGrpCd.equals(QueryAppConst.APPLICATION_SUB_GROUP)) {
            return queryProp.erdSchemaSubName()

        } else if (dependencyGrpCd.equals(QueryAppConst.APPLICATION_BI_MAIN_GROUP)) {
            return queryProp.erdSchemaSubName()

        } else if (dependencyGrpCd.equals(QueryAppConst.APPLICATION_BI_SUB_GROUP)) {
            return queryProp.erdSchemaSubName()
        }
    }

    /**
     * request schema name by dependency group
     *
     *
     * @param dependencyGrpCd
     * @return
     */
    public requestOuterFaceSchemaName(String dependencyGrpCd) {
        if (dependencyGrpCd.equals(QueryAppConst.APPLICATION_MAIN_GROUP)) {
            return queryProp.erdSchemaOuterFaceName()

        } else if (dependencyGrpCd.equals(QueryAppConst.APPLICATION_SUB_GROUP)) {
            return queryProp.erdSchemaOuterFaceSubName()
        }
    }
}
