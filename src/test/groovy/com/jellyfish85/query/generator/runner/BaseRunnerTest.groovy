package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.utils.QueryAppProp
import org.dbunit.operation.DatabaseOperation
import org.junit.AfterClass
import org.junit.BeforeClass

import java.sql.Connection
import java.sql.DatabaseMetaData

import org.dbunit.database.DatabaseConnection
import org.dbunit.database.IDatabaseConnection
import org.dbunit.dataset.IDataSet
import org.dbunit.dataset.excel.XlsDataSet
//import org.dbunit.dataset.csv.CsvDataSetWriter
//import org.dbunit.dataset.xml.FlatDtdDataSet
//import org.dbunit.dataset.xml.FlatXmlDataSet

/**
 *
 *
 *
 */
class BaseRunnerTest extends GroovyTestCase {

    String        dependentGrpCd = null
    String       _environment    = null
    BaseRunner   runner          = null
    QueryAppProp _queryProp      = null

    @BeforeClass
    void setUp() {
        dependentGrpCd  = System.getProperty("4test.dependentGrpCd")
        _environment    = System.getProperty("4test.environment")
        runner          = new BaseRunner(dependentGrpCd, _environment)

        _queryProp = new QueryAppProp(_environment)

        runner.databaseInitialize()

        Connection conn = runner.getConnection()
        IDatabaseConnection iConn = new DatabaseConnection(conn, _queryProp.erdSchema4Test())

        String url = "/com/jellyfish85/query/generator/excel/KR_OBJECT_DEPENDENCIES.xls"
        File file  = new File(getClass().getResource(url).toURI())
        FileInputStream inputStream = new FileInputStream(file)
        IDataSet partialDataSet     = new XlsDataSet(inputStream)

        DatabaseOperation.CLEAN_INSERT.execute(iConn, partialDataSet)
        conn.commit()
    }

    @AfterClass
    void tearDown() {
        runner.databaseFinalize()
    }

    void testGetConnection() {
        // check Connection commit mode
        Connection conn = runner.getConnection()
        assertEquals("auto commit should be true", conn.getAutoCommit(), false)

        // confirm test schema name
        DatabaseMetaData metaData = conn.getMetaData()
        assertEquals("schemaName should be test schema name",
                metaData.getUserName(), _queryProp.erdSchema4Test())
    }

    void testGetDependencies() {
        // check dependencies list
        ArrayList<KrObjectDependenciesBean> beans = runner.getDependencies()
        assertEquals("beans size should be 3.", 3, beans.size())

        beans.each {bean ->
            assert bean instanceof KrObjectDependenciesBean
        }
    }

    // check existent of output folder
    void testInitializeOutputFolder() {
        File outputFolder = new File(_queryProp.outputFolder())

        assertEquals("output folder exists", Boolean.TRUE, outputFolder.exists())
    }
}
