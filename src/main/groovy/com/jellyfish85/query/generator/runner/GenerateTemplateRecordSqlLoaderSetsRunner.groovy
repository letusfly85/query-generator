package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrObjectDependenciesDao
import com.jellyfish85.query.generator.downloader.FileDownloader
import com.jellyfish85.query.generator.generator.TemplateRecordGenerator
import com.jellyfish85.query.generator.helper.ResourceCopyHelper
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.TemplateRecordXlsBean
import com.jellyfish85.xlsaccessor.utils.XlsAppProp
import org.apache.commons.io.FileUtils


class GenerateTemplateRecordSqlLoaderSetsRunner {

    public static void main(String[] args) {
        BaseRunner _context = new BaseRunner()
        _context.databaseInitialize()

        def conn      = _context.getConnection()
        def queryProp = _context.queryProp

        XlsAppProp        xlsAppProp      = new XlsAppProp()

        String dependencyGrpCd            = args[0]
        KrObjectDependenciesDao dependenciesDao = new KrObjectDependenciesDao()
        def _dependencies = dependenciesDao.findByDependencyGrpCd(conn, dependencyGrpCd)
        ArrayList<KrObjectDependenciesBean> dependencies = dependenciesDao.convert(_dependencies)

        SVNRequestBean _requestBean = new SVNRequestBean()
        _requestBean.setPath(xlsAppProp.templateRecordBookPath())
        FileDownloader.download(_requestBean)
        String _path = (new File(queryProp.applicationWorkspacePath(), _requestBean.path()))

        TemplateRecordGenerator generator =
                new TemplateRecordGenerator(_path, new BigDecimal(0), _requestBean, dependencies)

        generator.generateTemplateControlFile()
        ArrayList<TemplateRecordXlsBean> xlsBeans = generator.getXlsBeans()
        generator.generateTemplateDataFile(xlsBeans)
        generator.generateTemplateDeleteQuery(xlsBeans)
        generator.generateTemplateShellScript()

        SVNRequestBean __requestBean = new SVNRequestBean()
        __requestBean.setPath(xlsAppProp.templateRecordDocsPath())
        FileDownloader.downloadDir(__requestBean, queryProp.applicationTemplatePath())
        File docs = new File(queryProp.applicationTemplatePath())
        docs.listFiles().each {File doc ->
            File dest = new File(queryProp.sqlLoaderDatPath(), doc.getName())
            FileUtils.copyFile(doc, dest)
        }

        ResourceCopyHelper copyHelper = new ResourceCopyHelper()
        String loginSqlPath = (new File(queryProp.sqlLoaderParentPath(), "login.sql")).getPath()
        copyHelper.copyLoginSql(loginSqlPath)

        _context.databaseFinalize()
    }
}