package com.jellyfish85.query.generator.generator

import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.helper.CodeGeneratorHelper
import com.jellyfish85.query.generator.utils.QueryReplaceUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

/**
 * generate authority code suite
 *
 * @author wada shunsuke
 * @since  2014/01/02
 *
 */
class AuthorityCodeGenerator extends GeneralGenerator {

    private BaseContext context                = null

    private String schemaName                  = null

    private HashMap<String, String> tableNames = new HashMap<String, String>()

    private QueryReplaceUtils replaceUtils     = null

    public AuthorityCodeGenerator(BaseContext _context) {
        super(_context)
        this.context = super.getBaseContext()

        this.replaceUtils  = new QueryReplaceUtils(this.context.queryProp)
        this.schemaName    = context.tableNameHelper.requestMainSchemaName(context.dependentGrpCd)
    }

    public void generateControlFile() {
        File controlPath = new File(this.context.queryProp.applicationControlPath())

        ArrayList<File> destFiles = new ArrayList<>()
        controlPath.listFiles().each {File file ->
            File dest = new File(this.context.queryProp.sqlLoaderCtlPath(), file.getName())

            FileUtils.copyFile(file, dest)
            destFiles.add(dest)

            this.tableNames.put(
                    FilenameUtils.getBaseName(file.getName()), this.context.queryProp.sqlLoaderLoadExecutor()
            )
        }

        destFiles.each {File dest ->
            replaceUtils.addSchemaName2ControlFile(dest, this.schemaName)
        }
    }

    public void generateDataFile() {
        File dataPath = new File(this.context.queryProp.applicationDataPath())

        ArrayList<File> destFiles = new ArrayList<>()
        dataPath.listFiles().each {File file ->
            File dest = new File(this.context.queryProp.sqlLoaderDatPath(), file.getName())

            FileUtils.copyFile(file, dest)
            replaceUtils.convertSJIS2UTF8(dest)
            destFiles.add(dest)
        }
    }

    public void generateShellScript() {
        CodeGeneratorHelper helper = new CodeGeneratorHelper()

        helper.generateLoadingShellScript(tableNames, this.context.fileNameHelper.requestSqlLoaderPath4AuthorityCode())
    }

}