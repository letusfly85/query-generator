package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.converter.XlsColumnAttribute2MsTabColumnsConverter
import com.jellyfish85.query.generator.helper.TableNameHelper
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.UniqueCodeXlsBean
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.XlsColumnAttribute
import com.jellyfish85.xlsaccessor.dao.query.generate.tool.UniqueCodeXlsDao

/**
 * generate unique code release sets including shell, control, data files
 *
 *
 * @author wada shunsuke
 * @since  2013/12/23
 *
 */
class UniqueCodeGenerator extends GeneralGenerator {

    private TableNameHelper  tableNameHelper = new TableNameHelper()

    public void generateUniqueCodeDataFile() {

    }

    /**
     * generate control file from unique code excel books
     *
     *
     * @author  wada shunsuke
     * @since   2013/12/27
     * @param path
     */
    public void generateUniqueCodeControlFile(ArrayList<KrObjectDependenciesBean> dependencies, String path) {
        UniqueCodeXlsDao xlsDao = new UniqueCodeXlsDao(path)

        XlsColumnAttribute2MsTabColumnsConverter converter =
                new XlsColumnAttribute2MsTabColumnsConverter()

        // get data from xls
        UniqueCodeXlsBean xlsBean = xlsDao.getHeaderInfo()

        String tableName = xlsBean.physicalTableName()

        String schemaName =
                tableNameHelper.findByApplicationGroupCd(dependencies, tableName).
                        objectOwnerAttr().value()

        // generate MS_TAB_COLUMNS list
        ArrayList<XlsColumnAttribute> attributes   = xlsDao.getCodeDefine()
        ArrayList<MsTabColumnsBean>   columnsBeans = converter.convert(attributes)

        MsTabColumnsBean beanIns = new MsTabColumnsBean()
        MsTabColumnsBean beanPln = new MsTabColumnsBean()
        MsTabColumnsBean beanUsr = new MsTabColumnsBean()
        MsTabColumnsBean beanFnc = new MsTabColumnsBean()
        beanIns.physicalColumnNameAttr().setValue(queryProp.sqlLoaderColumnTimestampDefault())
        beanPln.physicalColumnNameAttr().setValue(queryProp.sqlLoaderColumnTimestampUpdate())
        beanUsr.physicalColumnNameAttr().setValue(queryProp.sqlLoaderColumnUser())
        beanFnc.physicalColumnNameAttr().setValue(queryProp.sqlLoaderColumnFunction())

        columnsBeans.addAll([beanIns, beanPln, beanUsr, beanFnc])

        Map map = [
                schemaName  : schemaName,
                tableName   : tableName,
                columnList  : columnsBeans
        ]

        String template = "/com/jellyfish85/query/generator/template/dml/controlFile.template"

        this.generate(map, template)
        String sqlLoaderControlPath = this.fileNameHelper.requestSqlLoaderControlPath(tableName)
        this.setPath(sqlLoaderControlPath)
        this.writeAppFile()
    }
}