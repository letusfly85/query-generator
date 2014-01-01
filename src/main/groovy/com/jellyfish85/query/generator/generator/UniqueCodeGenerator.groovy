package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTablesBean
import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.constant.QueryAppConst
import com.jellyfish85.query.generator.converter.XlsColumnAttribute2MsTabColumnsConverter
import com.jellyfish85.query.generator.helper.TableNameHelper
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.UniqueCodeXlsBean
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.XlsColumnAttribute
import com.jellyfish85.xlsaccessor.dao.query.generate.tool.UniqueCodeXlsDao
import org.apache.commons.io.FileUtils
import org.apache.commons.lang.StringUtils

/**
 * generate unique code release sets including shell, control, data files
 *
 *
 * @author wada shunsuke
 * @since  2013/12/23
 *
 */
class UniqueCodeGenerator extends GeneralGenerator {

    private String path               =  QueryAppConst.STRING_BLANK

    private String schemaName         =  QueryAppConst.STRING_BLANK

    XlsColumnAttribute2MsTabColumnsConverter converter =
            new XlsColumnAttribute2MsTabColumnsConverter()

    private TableNameHelper  tableNameHelper = new TableNameHelper()

    private UniqueCodeXlsDao  xlsDao  = null

    private UniqueCodeXlsBean xlsBean = null

    public UniqueCodeGenerator() {
    }

    public void setTableNames(HashMap<String, String> map) {
        map.put(this.xlsBean.physicalTableName(), queryProp.sqlLoaderLoadExecutor())
    }

    /**
     * remove exceptional files
     *
     *
     * @param files
     * @return
     */
    public ArrayList<File> removeSpecialCodes(File _parentPath) {
        ArrayList<File> removedFiles = new ArrayList<>()

        Boolean _switch = true
        _parentPath.listFiles().each {File file ->
            _switch = true
            queryProp.exceptCodeDefault().each {key, value ->
                if (file.getName().matches(".*" + value + ".*")) {
                    _switch = false
                }
            }

            queryProp.exceptCodeMaintenance().each {key, value ->
                if (file.getName().matches(".*" + value + ".*")) {
                    _switch = false
                }
            }

            if (_switch){
                removedFiles.add(file)
            }
        }

        return removedFiles
    }

    /**
     * initialize bean info, add table name and other attributes
     *
     *
     * @param _dependencies
     * @param _path
     */
    public void initializeBean(
            ArrayList<KrObjectDependenciesBean> _dependencies,
            String _path
    ) {
        this.path = _path
        this.xlsDao = new UniqueCodeXlsDao(this.path)

        // get data from xls
        this.xlsBean = xlsDao.getHeaderInfo()

        String tableName = xlsBean.physicalTableName()

        this.schemaName =
                tableNameHelper.findByApplicationGroupCd(_dependencies, tableName).
                        objectOwnerAttr().value()
    }

    /**
     * generate data file from excel file
     *
     *
     */
    public void generateUniqueCodeDataFile() {
        ArrayList<ArrayList<String>> listList = xlsDao.getDataEntry()

        ArrayList<String> datEntries = new ArrayList<>()
        listList.each {ArrayList<String> list ->
            def datEntry = "\""
            datEntry +=
                    StringUtils.join(list.collect{it.replace("\"", "\"\"")}, "\",\"")

            datEntry += "\"${QueryAppConst.STRING_DAT_END}"
            datEntries.add(datEntry)
        }

        MsTablesBean tablesBean = new MsTablesBean()
        tablesBean.physicalTableNameAttr().setValue(xlsBean.physicalTableName())
        String datPath = this.fileNameHelper.requestSqlLoaderDataPath(tablesBean)
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
     * generate control file from unique code excel books
     *
     *
     * @author  wada shunsuke
     * @since   2013/12/27
     * @dependencies
     *
     */
    public void generateUniqueCodeControlFile() {
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

        beanIns.dataDefaultAttr().setValue(queryProp.sqlLoaderDefaultValueTimestamp())
        beanPln.dataDefaultAttr().setValue(queryProp.sqlLoaderDefaultValueTimestamp())
        beanUsr.dataDefaultAttr().setValue(queryProp.sqlLoaderDefaultValueUserId())
        beanFnc.dataDefaultAttr().setValue(queryProp.sqlLoaderDefaultValueFunctionId())


        columnsBeans.addAll([beanIns, beanPln, beanUsr, beanFnc])

        String tableName = xlsBean.physicalTableName()
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