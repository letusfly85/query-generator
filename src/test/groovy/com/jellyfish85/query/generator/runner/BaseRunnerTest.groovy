package com.jellyfish85.query.generator.runner

import com.jellyfish85.query.generator.utils.QueryAppProp

import java.sql.Connection
import java.sql.DatabaseMetaData

/**
 *
 *
 *
 */
class BaseRunnerTest extends GroovyTestCase {

    String      dependentGrpCd = System.getProperty("4test.dependentGrpCd")
    String     _environment    = System.getProperty("4test.environment")
    BaseRunner runner          = new BaseRunner(dependentGrpCd, _environment)

    QueryAppProp _queryProp = new QueryAppProp(_environment)

    void setUp() {
        runner.databaseInitialize()
    }

    void tearDown() {
        runner.databaseFinalize()
    }

    void testGetConnection() {

        // check Connection commit mode
        Connection conn = runner.getConnection()
        assertEquals("auto commit should be true", conn.getAutoCommit(), false)

        // confirm test schema name
        DatabaseMetaData metaData = conn.getMetaData()
        assertEquals("schemaName", metaData.getUserName(), _queryProp.erdSchema4Test())
    }

    /*
    void testGetDependencies() {

    }
    */
}
