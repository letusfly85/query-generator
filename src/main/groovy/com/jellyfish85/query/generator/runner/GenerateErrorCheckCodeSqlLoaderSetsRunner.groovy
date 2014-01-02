package com.jellyfish85.query.generator.runner

import com.jellyfish85.query.generator.downloader.FileDownloader
import com.jellyfish85.query.generator.generator.ErrorCheckCodeGenerator
import com.jellyfish85.svnaccessor.bean.SVNRequestBean

/**
 *
 *
 */
class GenerateErrorCheckCodeSqlLoaderSetsRunner {

    public static void main(String[] args) {
        BaseRunner _context = new BaseRunner()
        _context.databaseInitialize()

        def queryProp = _context.queryProp
        def dependencyGrpCd = args[0]

        SVNRequestBean controlRequestBean = new SVNRequestBean()
        controlRequestBean.setPath(queryProp.subversionErrorCheckCodeControlPath())
        SVNRequestBean dataRequestBean = new SVNRequestBean()
        dataRequestBean.setPath(queryProp.subversionErrorCheckCodeDataPath())

        FileDownloader.downloadDir(controlRequestBean, queryProp.applicationControlPath())
        FileDownloader.downloadDir(dataRequestBean,    queryProp.applicationDataPath())

        ErrorCheckCodeGenerator generator = new ErrorCheckCodeGenerator(dependencyGrpCd)
        generator.generateControlFile()
        generator.generateDataFile()
        generator.generateShellScript()

        _context.databaseFinalize()
    }
}
