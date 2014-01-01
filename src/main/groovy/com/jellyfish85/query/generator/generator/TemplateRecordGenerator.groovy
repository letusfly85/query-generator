package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.constant.QueryAppConst
import com.jellyfish85.query.generator.helper.TableNameHelper
import com.jellyfish85.query.generator.utils.QueryAppProp
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.TemplateRecordXlsBean
import com.jellyfish85.xlsaccessor.constant.AppConst
import com.jellyfish85.xlsaccessor.dao.query.generate.tool.TemplateRecordXlsDao
import com.jellyfish85.xlsaccessor.utils.XlsAppProp
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

    private TableNameHelper tableNameHelper = new TableNameHelper()

    private XlsAppProp      xlsAppProp      = new XlsAppProp()

    public TemplateRecordGenerator(
            String _path, BigDecimal _ticketNumber, SVNRequestBean _requestBean,
            ArrayList<KrObjectDependenciesBean> dependencies
    ) {
        this.path         = _path
        this.ticketNumber = _ticketNumber
        this.requestBean  = _requestBean

        xlsDao = new TemplateRecordXlsDao(this.path)

        this.tableName  = xlsAppProp.templateRecordDefinePhysicalTableName()
        this.schemaName =
                tableNameHelper.findByApplicationGroupCd(dependencies, tableName).
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
                columnName  : this.xlsAppProp.templateRecordDefineColumnMap("recordId"),
                recordIds   : recordIds
        ]

        String template = "/com/jellyfish85/query/generator/template/dml/deleteTemplateRecord.template"

        this.generate(map, template)
        String sqlLoaderDeleteTablePath =
                this.fileNameHelper.requestSqlLoaderDeleteTablePath(this.schemaName, this.tableName)
        this.setPath(sqlLoaderDeleteTablePath)
        this.writeAppFile()

    }

    public void generateTemplateShellScript() {

    }

    /**
     *
     *
     * @todo
     */
    public void generateTemplateControlFile() {
        ArrayList<MsTabColumnsBean> _columnList = new ArrayList<>()

        MsTabColumnsBean beanIns = new MsTabColumnsBean()
        MsTabColumnsBean beanPln = new MsTabColumnsBean()
        MsTabColumnsBean beanUsr = new MsTabColumnsBean()
        MsTabColumnsBean beanFnc = new MsTabColumnsBean()
        MsTabColumnsBean beanFlg = new MsTabColumnsBean()

        beanIns.physicalColumnNameAttr().setValue(queryProp.sqlLoaderColumnTimestampDefault())
        beanPln.physicalColumnNameAttr().setValue(queryProp.sqlLoaderColumnTimestampUpdate())
        beanUsr.physicalColumnNameAttr().setValue(queryProp.sqlLoaderColumnUser())
        beanFnc.physicalColumnNameAttr().setValue(queryProp.sqlLoaderColumnFunction())
        beanFlg.physicalColumnNameAttr().setValue(queryProp.sqlLoaderColumnLogicalDelete())

        beanIns.dataDefaultAttr().setValue(queryProp.sqlLoaderDefaultValueTimestamp())
        beanPln.dataDefaultAttr().setValue(queryProp.sqlLoaderDefaultValueTimestamp())
        beanUsr.dataDefaultAttr().setValue(queryProp.sqlLoaderDefaultValueUserId())
        beanFnc.dataDefaultAttr().setValue(queryProp.sqlLoaderDefaultValueFunctionId())

        _columnList.addAll([beanFlg, beanIns, beanPln, beanUsr, beanFnc])

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
                            ,QueryAppConst.ORGANIZATION
                            ,QueryAppConst.APPLICATION_NAME], "\",\"")

            dataEntry += "\"${QueryAppConst.STRING_DAT_END}"
            dataEntries.add(dataEntry)
        }

        MsTablesBean tablesBean = new MsTablesBean()
        tablesBean.physicalTableNameAttr().setValue(xlsBeans.head().physicalTableName())
        String dataPath = this.fileNameHelper.requestSqlLoaderDataPath(tablesBean)
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