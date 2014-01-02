package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.dbaccessor.manager.DatabaseManager
import com.jellyfish85.query.generator.BaseContext

import java.sql.Connection

public class BaseRunner {

    public BaseContext _context = null
    public BaseRunner(String dependentGrpCd, String environment) {
        this._context  = new BaseContext(dependentGrpCd, environment)
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
