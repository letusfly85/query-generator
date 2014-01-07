package com.jellyfish85.query.generator.generator

import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.helper.CodeGeneratorHelper
import com.jellyfish85.query.generator.utils.QueryReplaceUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.StringUtils

/**
 * generate error check code suites
 *
 *
 * @author wada shunsuke
 * @since  2014/01/02
 */
class ErrorCheckCodeGenerator extends GeneralGenerator {

    private String schemaName                  = null

    private HashMap<String, String> tableNames = new HashMap<String, String>()

    private QueryReplaceUtils replaceUtils     = null

    private BaseContext context                = null

    public ErrorCheckCodeGenerator(BaseContext _context) {
        super(_context)
        this.context = super.getBaseContext()
        this.replaceUtils = new QueryReplaceUtils(this.context.queryProp)

        this.schemaName = this.context.tableNameHelper.requestMainSchemaName(this.context.dependentGrpCd)
    }

    public void generateControlFile() {
        File controlPath = new File(this.context.queryProp.applicationControlPath())

        ArrayList<File> destFiles = new ArrayList<>()
        controlPath.listFiles().each {File file ->
            String extName  = FilenameUtils.getExtension(file.getName()).toLowerCase()
            String fileName = StringUtils.join([FilenameUtils.getBaseName(file.getName()), extName], ".")
            File dest = new File(this.context.queryProp.sqlLoaderCtlPath(), fileName)

            FileUtils.copyFile(file, dest)
            destFiles.add(dest)

            this.tableNames.put(
                    FilenameUtils.getBaseName(file.getName()), this.context.queryProp.sqlLoaderLoadExecutor()
            )
        }

        destFiles.each {File dest ->
            //replaceUtils.convertSJIS2UTF8(dest)
            replaceUtils.addSchemaName2ControlFile(dest, this.schemaName)
        }
    }

    public void generateDataFile() {
        File dataPath = new File(this.context.queryProp.applicationDataPath())

        ArrayList<File> destFiles = new ArrayList<>()
        dataPath.listFiles().each {File file ->
            String extName  = FilenameUtils.getExtension(file.getName()).toLowerCase()
            String fileName = StringUtils.join([FilenameUtils.getBaseName(file.getName()), extName], ".")
            File dest = new File(this.context.queryProp.sqlLoaderDatPath(), fileName)

            FileUtils.copyFile(file, dest)
            destFiles.add(dest)
        }

        /*destFiles.each {File dest ->
            replaceUtils.convertSJIS2UTF8(dest)
        }*/
    }

    public void generateShellScript() {
        CodeGeneratorHelper helper = new CodeGeneratorHelper()

        helper.generateLoadingShellScript(tableNames, this.context.fileNameHelper.requestSqlLoaderPath4ErrorCheckCode())
    }
}
