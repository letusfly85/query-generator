package com.jellyfish85.query.generator.runner

import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.downloader.FileDownloader
import com.jellyfish85.query.generator.generator.ErrorCheckCodeGenerator
import com.jellyfish85.svnaccessor.bean.SVNRequestBean

/**
 *
 *
 */
class GenerateErrorCheckCodeSqlLoaderSetsRunner {

    public static void main(String[] args) {
        def dependencyGrpCd = args[0]
        def environment     = args[1]

        BaseRunner  runner  = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context = runner._context
        runner.databaseInitialize()

        def queryProp = context.queryProp

        SVNRequestBean controlRequestBean = new SVNRequestBean()
        controlRequestBean.setPath(queryProp.subversionErrorCheckCodeControlPath())
        SVNRequestBean dataRequestBean = new SVNRequestBean()
        dataRequestBean.setPath(queryProp.subversionErrorCheckCodeDataPath())

        FileDownloader.downloadDir(controlRequestBean, queryProp.applicationControlPath())
        FileDownloader.downloadDir(dataRequestBean,    queryProp.applicationDataPath())

        ErrorCheckCodeGenerator generator = new ErrorCheckCodeGenerator(context)
        generator.generateControlFile()
        generator.generateDataFile()
        generator.generateShellScript()

        runner.databaseFinalize()
    }
}
