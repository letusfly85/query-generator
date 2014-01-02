package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.dbaccessor.manager.DatabaseManager
import com.jellyfish85.query.generator.helper.ArgumentsHelper
import com.jellyfish85.query.generator.utils.QueryAppProp

import java.sql.Connection

public class BaseRunner {

    public BaseRunner() {}

    private static DatabaseManager manager = new DatabaseManager()

    private static ArgumentsHelper argsHelper  = new ArgumentsHelper()

    public QueryAppProp queryProp = new QueryAppProp()

    public ArgumentsHelper getArgsHelper() {
        return this.argsHelper
    }

    public Connection getConnection() {
        return this.manager.conn()
    }

    public void databaseInitialize() {
        println("database initialize..")
        println(".. getting connection ..")
        this.manager.connect()
    }

    public void databaseFinalize() {
        this.manager.jCommit()
        this.manager.jClose()
    }

    /**
     * return dependency sets
     *
     * @param dependencyGrpCd
     * @return
     */
    public ArrayList<KrObjectDependenciesBean> getDependencies(
            String dependencyGrpCd

    ) {
        KrObjectDependenciesDao dependenciesDao = new KrObjectDependenciesDao()
        def _dependencies =
                dependenciesDao.findByDependencyGrpCd(this.connection, dependencyGrpCd)

        ArrayList<KrObjectDependenciesBean> dependencies = dependenciesDao.convert(_dependencies)
        return dependencies
    }

}
