package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTabColumnsDao
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTablesDao
import com.jellyfish85.query.generator.generator.restoreQueryGenerator

/**
 * == resotreQueryGeneratorRunner ==
 *
 * @author wada shunsuke
 * @since  2013/12/02
 *
 */
class resotreQueryGeneratorRunner {

    public static void main(String[] args) {

        BaseRunner _context = new BaseRunner()
        _context.databaseInitialize()

        def conn = _context.getConnection()

        MsTablesDao msTablesDao         = new MsTablesDao()
        MsTabColumnsDao msTabColumnsDao = new MsTabColumnsDao()

        String tableName = args[0]

        MsTablesBean msTablesBean = new MsTablesBean()
        msTablesBean.physicalTableNameAttr().setValue(tableName)

        msTablesBean =  msTablesDao.findOne(conn, msTablesBean)

        def _sets = msTabColumnsDao.find(conn, msTablesBean)
        ArrayList<MsTabColumnsBean> sets = msTabColumnsDao.convert(_sets)

        println(sets.size())
        sets.each {MsTabColumnsBean bean -> println(bean.physicalColumnNameAttr().value())}

        restoreQueryGenerator generator = new restoreQueryGenerator()

        def query = generator.generate(sets)

        println(query)

        _context.databaseFinalize()
    }
}
