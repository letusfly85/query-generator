package com.jellyfish85.query.generator.helper

import com.jellyfish85.query.generator.constant.QueryAppConst
import com.jellyfish85.query.generator.utils.QueryAppProp
import com.jellyfish85.xlsaccessor.constant.AppConst

import javax.management.Query

/**
 * test class of TableNameHelper
 *
 *
 */
class TableNameHelperTest extends GroovyTestCase {

    QueryAppProp _queryProp = new QueryAppProp("test")
    TableNameHelper tableNameHelper = new TableNameHelper(_queryProp)

    void testRequestBKTableName() {
        String bkTableName = tableNameHelper.requestBKTableName("MY_TABLE")
        assertEquals("table name should be BK_MY_TABLE", "BK_MY_TABLE", bkTableName)
    }

    void testRequestBITableName() {
        String biTableName = tableNameHelper.requestBITableName("M_BI_TABLE")
        assertEquals("table name should be C_BI_TABLE", "C_BI_TABLE", biTableName)
    }

    void testRequestMainSchemaName() {
        String mainSchemaName =
                tableNameHelper.requestMainSchemaName(QueryAppConst.APPLICATION_MAIN_GROUP)
        assertEquals("schema name should be main group schema", _queryProp.erdSchemaMainName(), mainSchemaName)

        String subSchemaName =
                tableNameHelper.requestMainSchemaName(QueryAppConst.APPLICATION_SUB_GROUP)
        assertEquals("schema name should be sub group schema",  _queryProp.erdSchemaSubName(), subSchemaName)
    }

    /*
    void testRequestBITableName1() {

    }



    void testRequestBKTableName1() {

    }

    void testIdentifyTableGroup() {

    }

    void testIsIkTable() {

    }

    void testConsistencyCheckExists() {

    }

    void testFindByApplicationGroupCd() {

    }



    void testRequestOuterFaceSchemaName() {

    }
    */
}
