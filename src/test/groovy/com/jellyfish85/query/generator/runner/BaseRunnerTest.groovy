package com.jellyfish85.query.generator.runner

import com.jellyfish85.query.generator.utils.QueryAppProp

import java.sql.Connection

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

    void testGetConnection() {
        runner.databaseInitialize()
        Connection conn = runner.getConnection()

        assertEquals("auto commit should be true", conn.getAutoCommit(), false)
    }

    /*
    void testGetDependencies() {

    }
    */
}
