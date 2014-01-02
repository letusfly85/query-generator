package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.constant.QueryAppConst
import com.jellyfish85.query.generator.converter.XlsColumnAttribute2MsTabColumnsConverter
import com.jellyfish85.query.generator.helper.TableNameHelper
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.TemplateRecordXlsBean
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.XlsColumnAttribute
import com.jellyfish85.xlsaccessor.constant.AppConst
import com.jellyfish85.xlsaccessor.dao.query.generate.tool.TemplateRecordXlsDao
import com.jellyfish85.xlsaccessor.utils.XlsAppProp
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.StringUtils

/**
 *
 *
 *
 */
class TemplateRecordGenerator extends GeneralGenerator {

    private String path = AppConst.STRING_BLANK()

    private TemplateRecordXlsDao xlsDao     = null

    private BigDecimal ticketNumber         = null

    private SVNRequestBean requestBean      = null

    private schemaName                      = null

    private tableName                       = null

    private XlsAppProp      xlsProp         = new XlsAppProp()

    public  BaseContext context             = null

    public TemplateRecordGenerator(
            BaseContext _context,
            String _path, BigDecimal _ticketNumber, SVNRequestBean _requestBean,
            ArrayList<KrObjectDependenciesBean> dependencies
    ) {
        super(_context)
        this.context = super.getBaseContext()

        this.path         = _path
        this.ticketNumber = _ticketNumber
        this.requestBean  = _requestBean

        xlsDao = new TemplateRecordXlsDao(this.path)

        this.tableName  = xlsProp.templateRecordDefinePhysicalTableName()
        this.schemaName =
                this.context.tableNameHelper.findByApplicationGroupCd(dependencies, tableName).
                        objectOwnerAttr().value()
    }

    public ArrayList<TemplateRecordXlsBean> getXlsBeans() {
        def _xlsBeans = xlsDao.findAll(ticketNumber, requestBean)

        return xlsDao.convert(_xlsBeans)
    }

    public void generateTemplateDeleteQuery(ArrayList<TemplateRecordXlsBean> xlsBeans) {
        ArrayList<String> recordIds = new ArrayList<>()
        xlsBeans.each {recordIds.add(it.recordId())}

        Map map = [
                schemaName  : this.schemaName,
                tableName   : this.tableName,
                columnName  : xlsDao.beanRecordId().physicalColumnName(),
                recordIds   : recordIds,
                executor    : this.context.queryProp.sqlLoaderLoadExecutor()
        ]

        String template = "/com/jellyfish85/query/generator/template/dml/deleteTemplateRecord.template"

        this.generate(map, template)
        String sqlLoaderDeleteTablePath =
                this.context.fileNameHelper.requestSqlLoaderDeleteTablePath(this.schemaName, this.tableName)
        this.setPath(sqlLoaderDeleteTablePath)
        this.writeAppFile()

    }

    public void generateTemplateShellScript() {
        String deleteTemplateRecordTablePath =
                FilenameUtils.getName(this.context.fileNameHelper.requestSqlLoaderDeleteTablePath(this.schemaName, this.tableName))
        Map map = [
                deleteTemplateRecordTablePath: deleteTemplateRecordTablePath,
                schemaName                   : this.context.queryProp.sqlLoaderLoadExecutor(),
                tableName                    : tableName
        ]

        String path = "/com/jellyfish85/query/generator/template/shell/execSqlLoader4Template.template"

        this.generate(map, path)
        this.setPath(this.context.fileNameHelper.requestSqlLoaderPath4TemplateRecord())
        this.writeAppFile()
    }

    /**
     *
     *
     */
    public void generateTemplateControlFile() {
        ArrayList<MsTabColumnsBean> _columnList = new ArrayList<>()

        ArrayList<XlsColumnAttribute> columnAttributes = xlsDao.getHeader()
        XlsColumnAttribute2MsTabColumnsConverter converter =
                new XlsColumnAttribute2MsTabColumnsConverter()
        ArrayList<MsTabColumnsBean> _header = converter.convert(columnAttributes)
        _columnList.addAll(_header)

        MsTabColumnsBean beanFil = new MsTabColumnsBean()
        MsTabColumnsBean beanFid = new MsTabColumnsBean()
        MsTabColumnsBean beanRey = new MsTabColumnsBean()
        MsTabColumnsBean beanRet = new MsTabColumnsBean()
        MsTabColumnsBean beanIns = new MsTabColumnsBean()
        MsTabColumnsBean beanPln = new MsTabColumnsBean()
        MsTabColumnsBean beanUsr = new MsTabColumnsBean()
        MsTabColumnsBean beanFnc = new MsTabColumnsBean()
        MsTabColumnsBean beanFlg = new MsTabColumnsBean()

        beanFil.physicalColumnNameAttr().setValue(xlsProp.templateRecordDefineColumnOptionFile())
        beanFid.physicalColumnNameAttr().setValue(xlsProp.templateRecordDefineColumnOptionFileData())
        beanRey.physicalColumnNameAttr().setValue(xlsProp.templateRecordDefineColumnOptionRegisterYmd())
        beanRet.physicalColumnNameAttr().setValue(xlsProp.templateRecordDefineColumnOptionRegisterTimestamp())
        beanIns.physicalColumnNameAttr().setValue(this.context.queryProp.sqlLoaderColumnTimestampDefault())
        beanPln.physicalColumnNameAttr().setValue(this.context.queryProp.sqlLoaderColumnTimestampUpdate())
        beanUsr.physicalColumnNameAttr().setValue(this.context.queryProp.sqlLoaderColumnUser())
        beanFnc.physicalColumnNameAttr().setValue(this.context.queryProp.sqlLoaderColumnFunction())
        beanFlg.physicalColumnNameAttr().setValue(this.context.queryProp.sqlLoaderColumnLogicalDelete())

        beanFil.dataDefaultAttr().setValue(this.context.queryProp.sqlLoaderDefaultValueFile())
        beanFid.dataDefaultAttr().setValue(this.context.queryProp.sqlLoaderDefaultValueFileData())
        beanRey.dataDefaultAttr().setValue(this.context.queryProp.sqlLoaderDefaultValueYmd())
        beanRet.dataDefaultAttr().setValue(this.context.queryProp.sqlLoaderDefaultValueTimestamp())
        beanIns.dataDefaultAttr().setValue(this.context.queryProp.sqlLoaderDefaultValueTimestamp())
        beanPln.dataDefaultAttr().setValue(this.context.queryProp.sqlLoaderDefaultValueTimestamp())
        beanUsr.dataDefaultAttr().setValue(this.context.queryProp.sqlLoaderDefaultValueUserId())
        beanFnc.dataDefaultAttr().setValue(this.context.queryProp.sqlLoaderDefaultValueFunctionId())
        beanFlg.dataDefaultAttr().setValue(this.context.queryProp.sqlLoaderDefaultValueCharZero())

        _columnList.addAll([beanFil, beanFid, beanRey, beanRet,
                beanFlg, beanIns, beanPln, beanUsr, beanFnc])

        Map map = [
                schemaName  : schemaName,
                tableName   : tableName,
                columnList  : _columnList,
                mode        : QueryAppConst.LOADER_WRITE_MODE_APPEND
        ]

        String path = "/com/jellyfish85/query/generator/template/dml/controlFile.template"

        this.generate(map, path)
        String sqlLoaderControlPath = this.context.fileNameHelper.requestSqlLoaderControlPath(tableName)
        this.setPath(sqlLoaderControlPath)
        this.writeAppFile()
    }


    public void generateTemplateDataFile(ArrayList<TemplateRecordXlsBean> xlsBeans) {
        ArrayList<String> dataEntries = new ArrayList<>()
        xlsBeans.each {TemplateRecordXlsBean xlsBean ->
            def dataEntry = "\""
            dataEntry +=
                    StringUtils.join([
                             xlsBean.recordId()
                            ,xlsBean.recordKind()
                            ,xlsBean.templateName()
                            ,xlsBean.displayName()
                            ,xlsBean.templatePath()
                            ,xlsBean.customerId()
                            ,"dat/" + xlsBean.templateName()], "\",\"")

            dataEntry += "\"${QueryAppConst.STRING_DAT_END}"
            dataEntries.add(dataEntry)
        }

        MsTablesBean tablesBean = new MsTablesBean()
        tablesBean.physicalTableNameAttr().setValue(xlsBeans.head().physicalTableName())
        String dataPath = this.context.fileNameHelper.requestSqlLoaderDataPath(tablesBean)
        FileUtils.forceMkdir((new File(dataPath)).getParentFile())
        PrintWriter pw = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(dataPath),"UTF-8")))

        dataEntries.eachWithIndex {entry, index ->
            pw.write(entry)

            if (!index.equals(dataEntries.size()-1)) {
                pw.write("\n")
            }
        }
        pw.close()

    }

}