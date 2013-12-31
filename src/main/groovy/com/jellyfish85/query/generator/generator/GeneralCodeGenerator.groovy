package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.query.generator.constant.QueryAppConst
import com.jellyfish85.query.generator.downloader.FileDownloader
import com.jellyfish85.query.generator.utils.QueryAppProp
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.GeneralCodeXlsBean
import com.jellyfish85.xlsaccessor.dao.query.generate.tool.GeneralCodeXlsDao
import com.jellyfish85.xlsaccessor.utils.XlsAppProp
import org.apache.commons.io.FileUtils
import org.apache.commons.lang.StringUtils

/**
 * generate general code sql loader, and sql maintenance scripts for release
 *
 *
 * @author wada shunsuke
 * @since  2013/12/16
 *
 */
class GeneralCodeGenerator extends GeneralGenerator {

    private XlsAppProp   xlsProp   = new XlsAppProp()

    private QueryAppProp queryProp = new QueryAppProp()

    /**
     * generate general code's data file
     *
     * @author wada shunsuke
     * @since  2013/12/20
     *
     */
    public void generateDataFile() {

        SVNRequestBean requestBean = new SVNRequestBean()
        requestBean.setPath(xlsProp.generalCodeBookPath())
        requestBean = FileDownloader.download(requestBean)

        String bookPath =
                StringUtils.join([queryProp.applicationWorkspacePath(), requestBean.path()], "/")

        GeneralCodeXlsDao codeXlsDao = new GeneralCodeXlsDao()
        def _codeXlsBeans =
                codeXlsDao.findAll(bookPath, new BigDecimal(0), requestBean)
        ArrayList<GeneralCodeXlsBean> codeXlsBeans = codeXlsDao.convert(_codeXlsBeans)

        ArrayList<String> datEntries = new ArrayList<>()
        codeXlsBeans.each {GeneralCodeXlsBean xlsBean ->
            def datEntry = "\""
            datEntry +=
                    StringUtils.join([
                     xlsBean.codeId()
                    ,xlsBean.codeValue()
                    ,xlsBean.logicalCodeName()
                    ,xlsBean.physicalCodeName()
                    ,xlsBean.logicalKeyName()
                    ,xlsBean.shortName()
                    ,xlsBean.displayOrder()
                    ,xlsBean.delFlg()
                    ,QueryAppConst.ORGANIZATION
                    ,QueryAppConst.APPLICATION_NAME], "\",\"")

            datEntry += "\"${QueryAppConst.STRING_DAT_END}"
            datEntries.add(datEntry)
        }

        MsTablesBean tablesBean = new MsTablesBean()
        tablesBean.physicalTableNameAttr().setValue(codeXlsBeans.head().physicalTableName())
        String datPath = this.fileNameHelper.requestSqlLoaderDatPath(tablesBean)
        File   datFile = new File(datPath)
        if (!datFile.getParentFile().exists()) {
            FileUtils.forceMkdir(datFile.getParentFile())
        }

        PrintWriter pw = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(datFile),"UTF-8")))

        datEntries.eachWithIndex {entry, index ->
            pw.write(entry)

            if (!index.equals(datEntries.size()-1)) {
                pw.write("\n")
            }
        }
        pw.close()
    }

    /**
     * generate general code's control file
     *
     * @param  schemaName
     * @param  columnList
     * @author wada shunsuke
     * @since  2013/12/20
     *
     */
    public void generateControlFile(String schemaName, ArrayList<MsTabColumnsBean> columnList) {
        String tableName = columnList.head().physicalTableNameAttr().value()
        ArrayList<MsTabColumnsBean> _columnList =
                columnList.findAll  {!it.physicalColumnNameAttr().value().matches("D([A-Z_]+)TIME([A-Z]+)P")}
        _columnList.each {it -> println(it.physicalColumnNameAttr().value())}
        MsTabColumnsBean beanIns = new MsTabColumnsBean()
        MsTabColumnsBean beanPln = new MsTabColumnsBean()
        beanIns.physicalColumnNameAttr().setValue(queryProp.sqlLoaderColumnTimestampDefault())
        beanPln.physicalColumnNameAttr().setValue(queryProp.sqlLoaderColumnTimestampUpdate())
        _columnList.add(beanIns)
        _columnList.add(beanPln)

        Map map = [
                schemaName  : schemaName,
                tableName   : tableName,
                columnList  : _columnList
        ]

        String path = "/com/jellyfish85/query/generator/template/dml/controlFile.template"

        this.generate(map, path)
        String sqlLoaderControlPath = this.fileNameHelper.requestSqlLoaderControlPath(tableName)
        this.setPath(sqlLoaderControlPath)
        this.writeAppFile()
    }
}