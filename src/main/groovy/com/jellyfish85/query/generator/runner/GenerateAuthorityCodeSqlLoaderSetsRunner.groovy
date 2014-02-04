package com.jellyfish85.query.generator.runner

import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.downloader.FileDownloader
import com.jellyfish85.query.generator.generator.AuthorityCodeGenerator
import com.jellyfish85.svnaccessor.bean.SVNRequestBean

/**
 *
 *
 * @author wada shunsuke
 * @since  2014/01/01
 *
 */
class GenerateAuthorityCodeSqlLoaderSetsRunner {

    public static void main(String[] args) {
        def dependencyGrpCd = args[0]
        def environment     = args[1]

        BaseRunner  runner  = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context = runner._context
        runner.databaseInitialize()

        def queryProp = context.queryProp

        SVNRequestBean controlRequestBean = new SVNRequestBean()
        controlRequestBean.setPath(queryProp.subversionAuthorityCodeControlPath())
        SVNRequestBean dataRequestBean = new SVNRequestBean()
        dataRequestBean.setPath(queryProp.subversionAuthorityCodeDataPath())

        FileDownloader.downloadDir(queryProp, controlRequestBean, queryProp.applicationControlPath())
        FileDownloader.downloadDir(queryProp, dataRequestBean,    queryProp.applicationDataPath())

        AuthorityCodeGenerator generator = new AuthorityCodeGenerator(context)
        generator.generateControlFile()
        generator.generateDataFile()
        generator.generateShellScript()

        runner.databaseFinalize()
    }

}
