package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.dao.erd.mainte.tool.MsTabColumnsDao
import com.jellyfish85.query.generator.generator.GeneralCodeGenerator
import com.jellyfish85.query.generator.helper.TableNameHelper
import com.jellyfish85.xlsaccessor.utils.XlsAppProp

/**
 * == GenerateSqlLoaderSetsRunner ==
 *
 *
 * @author wada shunsuke
 * @since  2013/12/08
 *
 */
class GenerateSqlLoaderSetsRunner {

    public static void main(String[] args) {
        BaseRunner _context = new BaseRunner()
        _context.databaseInitialize()

        def conn = _context.getConnection()

        XlsAppProp      xlsAppProp      = new XlsAppProp()
        String generalTableName = xlsAppProp.generalCodePhysicalTableName()

        String dependencyGrpCd          = args[0]
        TableNameHelper tableNameHelper = new TableNameHelper()
        String schemaName = tableNameHelper.findByApplicationGroupCd(dependencyGrpCd, generalTableName)

        MsTablesBean tablesBean = new MsTablesBean()
        tablesBean.physicalTableNameAttr().setValue(generalTableName)

        MsTabColumnsDao columnsDao = new MsTabColumnsDao()
        def _columnList = columnsDao.find(conn, tablesBean)
        ArrayList<MsTabColumnsBean> columnList = columnsDao.convert(_columnList)

        GeneralCodeGenerator generator = new GeneralCodeGenerator()
        generator.generateDataFile()
        generator.generateControlFile(schemaName, columnList)

    }
}