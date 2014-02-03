package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.downloader.FileDownloader
import com.jellyfish85.query.generator.generator.TemplateRecordGenerator
import com.jellyfish85.query.generator.helper.ResourceCopyHelper
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.TemplateRecordXlsBean
import org.apache.commons.io.FileUtils


class GenerateTemplateRecordSqlLoaderSetsRunner {

    public static void main(String[] args) {
        def dependencyGrpCd = args[0]
        def environment     = args[1]

        BaseRunner  runner  = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context = runner._context
        runner.databaseInitialize()

        def queryProp       = context.queryProp

        ArrayList<KrObjectDependenciesBean> dependencies = runner.getDependencies()

        SVNRequestBean _requestBean = new SVNRequestBean()
        _requestBean.setPath(queryProp.subversionTemplateRecordBookPath())
        FileDownloader.download(queryProp, _requestBean)
        String _path = (new File(queryProp.applicationWorkspacePath(), _requestBean.path()))

        TemplateRecordGenerator generator =
                new TemplateRecordGenerator(context, _path, new BigDecimal(0), _requestBean, dependencies)

        generator.generateTemplateControlFile()
        ArrayList<TemplateRecordXlsBean> xlsBeans = generator.getXlsBeans()
        generator.generateTemplateDataFile(xlsBeans)
        generator.generateTemplateDeleteQuery(xlsBeans)
        generator.generateTemplateShellScript()

        SVNRequestBean __requestBean = new SVNRequestBean()
        __requestBean.setPath(queryProp.subversionTemplateRecordDocsPath())
        FileDownloader.downloadDir(__requestBean, queryProp.applicationTemplatePath())
        File docs = new File(queryProp.applicationTemplatePath())
        docs.listFiles().each {File doc ->
            File dest = new File(queryProp.sqlLoaderDatPath(), doc.getName())
            FileUtils.copyFile(doc, dest)
        }

        ResourceCopyHelper copyHelper = new ResourceCopyHelper()
        String loginSqlPath = (new File(queryProp.sqlLoaderParentPath(), "login.sql")).getPath()
        copyHelper.copyLoginSql(loginSqlPath)

        runner.databaseFinalize()
    }
}