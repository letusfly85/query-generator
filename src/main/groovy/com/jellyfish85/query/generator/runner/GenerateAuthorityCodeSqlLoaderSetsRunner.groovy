package com.jellyfish85.query.generator.runner

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
        BaseRunner _context = new BaseRunner()
        _context.databaseInitialize()

        def queryProp = _context.queryProp
        def dependencyGrpCd = args[0]

        SVNRequestBean controlRequestBean = new SVNRequestBean()
        controlRequestBean.setPath(queryProp.subversionAuthorityCodeControlPath())
        SVNRequestBean dataRequestBean = new SVNRequestBean()
        dataRequestBean.setPath(queryProp.subversionAuthorityCodeDataPath())

        FileDownloader.downloadDir(controlRequestBean, queryProp.applicationControlPath())
        FileDownloader.downloadDir(dataRequestBean,    queryProp.applicationDataPath())

        AuthorityCodeGenerator generator = new AuthorityCodeGenerator(dependencyGrpCd)
        generator.generateControlFile()
        generator.generateDataFile()
        generator.generateShellScript()

        _context.databaseFinalize()
    }

}
