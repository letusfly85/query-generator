package com.jellyfish85.query.generator.generator

import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.utils.QueryAppProp
import org.junit.BeforeClass

/**
 * GeneralGenerator test class
 *
 *
 * @author wada shunsuke
 * @since  2014/01/13
 *
 */
class GeneralGeneratorTest extends GroovyTestCase {

    String       dependentGrpCd  = null
    String       _environment    = null
    BaseContext  context         = null
    QueryAppProp _queryProp      = null

    GeneralGenerator generator   = null

    @BeforeClass
    void setUp() {
        dependentGrpCd       = System.getProperty("4test.dependentGrpCd")
        _environment         = System.getProperty("4test.environment")
        this.context         = new BaseContext(dependentGrpCd, _environment)
        _queryProp           = this.context.queryProp

        this.generator  = new GeneralGenerator(this.context)
    }

    void testGetBaseContext() {
        // check object type
        assert this.context instanceof BaseContext
    }

    void testInitializeQuery() {
        // check query is String Blank
        generator.initializeQuery()

        assertEquals("after initialize, query should be blank", "", generator.getQuery())
    }

    void testSetQuery() {
        // check query set
        generator.initializeQuery()
        generator.setQuery("my.query")

        assertEquals("this query should be 'my.query'", "my.query", generator.getQuery())
    }

    void testAppendQuery() {
        // check query appending
        generator.initializeQuery()
        generator.setQuery("abc")
        generator.appendQuery("def")

        assertEquals("this query should be 'abcdef", "abcdef", generator.getQuery())
    }

    void testSetPath() {
        // check path set
        generator.setPath("my.path")

        assertEquals("this path should be 'my.path", "my.path", generator.getPath())
    }

    /* todo
    void testWriteAppFile() {

    }

    void testGenerate() {

    }
    */
}
