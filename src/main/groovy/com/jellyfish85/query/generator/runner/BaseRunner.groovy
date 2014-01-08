package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.dbaccessor.manager.DatabaseManager
import com.jellyfish85.query.generator.BaseContext
import org.apache.commons.io.FileUtils

import java.sql.Connection

/**
 * base class of all runners
 *
 * contain properties, helpers, database connection
 *
 * @author wada shunsuke
 * @since  2014/01/08
 *
 */
public class BaseRunner {

    private BaseContext _context    = null

    private String      environment = null

    /**
     *
     *
     *
     * @param dependentGrpCd
     * @param _environment
     */
    public BaseRunner(String dependentGrpCd, String _environment) {
        this.environment = _environment
        this._context     = new BaseContext(dependentGrpCd, this.environment)

        outputFolderClean()
    }

    private static DatabaseManager manager     = new DatabaseManager()
    public Connection getConnection() {
        return this.manager.conn()
    }

    public void databaseInitialize() {
        println("database initialize..")
        println(".. getting connection ..")
        this.manager.connect()
    }

    public void databaseFinalize() {
        println("database finalize..")
        println(".. closing connection ..")

        this.manager.jCommit()
        this.manager.jClose()
    }

    private void outputFolderClean() {
       if (environment.startsWith("patch")) {
           File outputFolder = new File(this._context.queryProp.outputFolder())
           FileUtils.cleanDirectory(outputFolder)
       }
    }

    /**
     * return dependency sets
     *
     *
     * @return
     */
    public ArrayList<KrObjectDependenciesBean> getDependencies() {
        KrObjectDependenciesDao dependenciesDao = new KrObjectDependenciesDao()
        def _dependencies =
                dependenciesDao.findByDependencyGrpCd(this.connection, this._context.dependentGrpCd)

        ArrayList<KrObjectDependenciesBean> dependencies = dependenciesDao.convert(_dependencies)
        return dependencies
    }

}
