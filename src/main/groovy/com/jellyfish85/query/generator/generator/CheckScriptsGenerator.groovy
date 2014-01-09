package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.query.generator.BaseContext

/**
 *
 *
 */
class CheckScriptsGenerator extends GeneralGenerator {

    private KrObjectDependenciesDao dao = new KrObjectDependenciesDao()

    private BaseContext context = null
    public CheckScriptsGenerator(BaseContext _context) {
        super(_context)
        this.context = super.getBaseContext()
    }

    public void generateErdExistence(ArrayList<String> schemaNames) {

        def key = "schemaNames"
        HashMap<String, ArrayList<String>> map = new HashMap()
        map.put(key, schemaNames)

        String checkObjectQueryPath = "/com/jellyfish85/query/generator/sql/check_object.sql"

        String checkQuery = dao.generateSQLIncludesList(checkObjectQueryPath, map)

        this.setQuery(checkQuery)

        this.writeAppFile()
    }
}
