package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.query.generator.downloader.FileDownloader
import com.jellyfish85.query.generator.generator.UniqueCodeGenerator
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.xlsaccessor.utils.XlsAppProp

/**
 *
 *
 *
 *
 */
class GenerateUniqueCodeSqlLoaderSetsRunner {

    public static void main(String[] args) {
        BaseRunner _context = new BaseRunner()
        _context.databaseInitialize()

        def conn      = _context.getConnection()
        def queryProp = _context.queryProp

        XlsAppProp      xlsAppProp      = new XlsAppProp()

        String dependencyGrpCd          = args[0]
        KrObjectDependenciesDao dependenciesDao = new KrObjectDependenciesDao()
        def _dependencies = dependenciesDao.findByDependencyGrpCd(conn, dependencyGrpCd)
        ArrayList<KrObjectDependenciesBean> dependencies = dependenciesDao.convert(_dependencies)
        UniqueCodeGenerator generator = new UniqueCodeGenerator()

        SVNRequestBean requestBean = new SVNRequestBean()
        requestBean.setPath(xlsAppProp.uniqueCodeBookParentPath())
        FileDownloader.downloadDir(requestBean)

        File parentPath =
                new File(queryProp.applicationWorkspacePath())

        parentPath.listFiles().each {File file ->
            println(file.getPath())
            generator.initializeBean(dependencies, file.getPath())

            generator.generateUniqueCodeControlFile(dependencies)
            generator.generateUniqueCodeDataFile(dependencies)
        }
    }
}