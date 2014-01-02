package com.jellyfish85.query.generator.generator

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

    private String dependencyGrpCd = null

    private String schemaName      = null

    private HashMap<String, String> tableNames = new HashMap<String, String>()

    private QueryReplaceUtils replaceUtils = new QueryReplaceUtils()

    public AuthorityCodeGenerator(String _dependencyGrpCd) {
        this.dependencyGrpCd = _dependencyGrpCd

        this.schemaName = this.tableNameHelper.requestMainSchemaName(this.dependencyGrpCd)
    }

    public void generateControlFile() {
        File controlPath = new File(queryProp.applicationControlPath())

        ArrayList<File> destFiles = new ArrayList<>()
        controlPath.listFiles().each {File file ->
            File dest = new File(queryProp.sqlLoaderCtlPath(), file.getName())

            FileUtils.copyFile(file, dest)
            destFiles.add(dest)

            this.tableNames.put(
                    FilenameUtils.getBaseName(file.getName()), queryProp.sqlLoaderLoadExecutor()
            )
        }

        destFiles.each {File dest ->
            replaceUtils.convertSJIS2UTF8(dest)
            replaceUtils.addSchemaName2ControlFile(dest, this.schemaName)
        }
    }

    public void generateDataFile() {
        File dataPath = new File(queryProp.applicationDataPath())

        ArrayList<File> destFiles = new ArrayList<>()
        dataPath.listFiles().each {File file ->
            File dest = new File(queryProp.sqlLoaderDatPath(), file.getName())

            FileUtils.copyFile(file, dest)
            destFiles.add(dest)
        }

        destFiles.each {File dest ->
            replaceUtils.convertSJIS2UTF8(dest)
        }
    }

    public void generateShellScript() {
        CodeGeneratorHelper helper = new CodeGeneratorHelper()

        helper.generateLoadingShellScript(tableNames, fileNameHelper.requestSqlLoaderPath4AuthorityCode())
    }

}