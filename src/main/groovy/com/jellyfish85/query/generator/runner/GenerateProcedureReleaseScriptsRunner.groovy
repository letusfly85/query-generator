package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.dao.src.mainte.tool.VChangesetsDao
import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.converter.VChangeSetsBean2SVNRequestBeanConverter
import com.jellyfish85.query.generator.downloader.FileDownloader
import com.jellyfish85.query.generator.generator.ProcedureReleaseScriptsGenerator
import com.jellyfish85.query.generator.helper.ResourceCopyHelper
import com.jellyfish85.svnaccessor.bean.SVNRequestBean

/**
 *
 *
 */
class GenerateProcedureReleaseScriptsRunner {

    public static void main(String[] args){
        def dependencyGrpCd = args[0]
        def environment     = args[1]
        def commitDate      = args[2]

        // generate context
        BaseRunner  runner  = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context = runner._context
        runner.databaseInitialize()

        def conn         = runner.getConnection()
        def dependencies = runner.getDependencies()
        def queryProp    = context.queryProp


        // specify target by commit date
        String procedurePath = queryProp.subversionProcedureDDLPathLike()
        VChangesetsDao changesetsDao = new VChangesetsDao()
        def _changesets = changesetsDao.findByCommitDateAndPath(conn, commitDate, procedurePath)

        VChangeSetsBean2SVNRequestBeanConverter converter =
                new VChangeSetsBean2SVNRequestBeanConverter()
        ArrayList<SVNRequestBean> requestBeans = converter.convertA(_changesets)

        // download target functions
        requestBeans.each {requestBean ->
            FileDownloader.download(queryProp, requestBean)
        }

        // generate ddl and release shell scripts
        //todo
        ProcedureReleaseScriptsGenerator generator =
                new ProcedureReleaseScriptsGenerator(context, requestBeans)
        generator.generateDDL()


        // add login sql to parent folder
        ResourceCopyHelper copyHelper = new ResourceCopyHelper()
        String loginSqlPath = (new File(queryProp.procedureDDLFolder(), "login.sql")).getPath()
        copyHelper.copyLoginSql(loginSqlPath)

    }
}
