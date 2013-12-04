package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTabColumnsDao
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTablesDao
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.query.generator.generator.RestoreQueryGenerator
import com.jellyfish85.query.generator.helper.TableNameHelper

/**
 * == ResotreQueryGeneratorRunner ==
 *
 * @author wada shunsuke
 * @since  2013/12/02
 *
 */
class ResotreQueryGeneratorRunner {

    public static void main(String[] args) {

        BaseRunner _context = new BaseRunner()
        _context.databaseInitialize()

        def conn = _context.getConnection()

        TableNameHelper helper = new TableNameHelper()

        KrObjectDependenciesDao  krObjectDependenciesDao =
                                          new KrObjectDependenciesDao()
        MsTablesDao msTablesDao         = new MsTablesDao()
        MsTabColumnsDao msTabColumnsDao = new MsTabColumnsDao()

        String tableName    = args[0]
        String dependencyCd = args[1]

        def _dependencySets =
            krObjectDependenciesDao.findByDependencyGrpCd(conn, dependencyCd)

        ArrayList<KrObjectDependenciesBean> dependencySets =
            krObjectDependenciesDao.convert(_dependencySets)
        KrObjectDependenciesBean dependency = helper.findByApplicationGroupCd(dependencySets, tableName)

        MsTablesBean msTablesBean = new MsTablesBean()
        msTablesBean.physicalTableNameAttr().setValue(tableName)

        msTablesBean =  msTablesDao.findOne(conn, msTablesBean)

        def _sets = msTabColumnsDao.find(conn, msTablesBean)
        ArrayList<MsTabColumnsBean> sets = msTabColumnsDao.convert(_sets)

        RestoreQueryGenerator generator = new RestoreQueryGenerator()

        def query = generator.generate(sets, dependency)

        println(query)

        _context.databaseFinalize()
    }
}
