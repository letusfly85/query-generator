package com.jellyfish85.query.generator.register

import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrReleaseDiffsBean
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrReleaseDiffsDao
import com.jellyfish85.dbaccessor.manager.DatabaseManager
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

class ReleaseDiffRegisterTest extends GroovyTestCase {

    ReleaseDiffRegister register = null

    String        dependentGrpCd = null
    String       _environment    = null
    QueryAppProp _queryProp      = null

    DatabaseManager manager      = null
    Connection      conn         = null

    @BeforeClass
    void setUp() {
        dependentGrpCd  = System.getProperty("4test.dependentGrpCd")
        _environment    = System.getProperty("4test.environment")

        _queryProp      = new QueryAppProp(_environment)

        manager         = new DatabaseManager()
        manager.connect()
        conn            = manager.conn()

        IDatabaseConnection iConn = new DatabaseConnection(conn, _queryProp.erdSchema4Test())

        String url = "/com/jellyfish85/query/generator/excel/KR_RELEASE_DIFFS.xls"
        File file  = new File(getClass().getResource(url).toURI())
        FileInputStream inputStream = new FileInputStream(file)
        IDataSet partialDataSet     = new XlsDataSet(inputStream)

        DatabaseOperation.CLEAN_INSERT.execute(iConn, partialDataSet)
        conn.commit()

        register = new ReleaseDiffRegister(conn, _queryProp.subversionUrlTag4Test(), _environment)
    }

    void testRegister() {
        register.setTagName("mybranch")
        register.setCurrentBeanTagName("mybranch")

        register.setAttributeFromDB()

        register.setSVNAttributeFromSVN()

        register.register()

        BigDecimal actualRevision = this.register.getLatestRevision()

        KrReleaseDiffsDao dao = new KrReleaseDiffsDao()
        def bean = new KrReleaseDiffsBean()
        bean.tagNameAttr().setValue("mybranch")
        def _list = dao.find(conn, bean)
        bean = dao.convert(_list).head()

        assertEquals("to revision should be true", actualRevision,  bean.toRevisionAttr().value())
    }
}