package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrObjectDependenciesBean
import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.generator.CheckScriptsGenerator

/**
 *
 *
 */
class GenerateCheckScriptsRunner {

    public static void main(String[] args) {
        def dependencyGrpCd = args[0]
        def environment     = args[1]

        BaseRunner  runner  = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context = runner._context
        runner.databaseInitialize()


        // specify schema names
        ArrayList<KrObjectDependenciesBean> beans = runner.getDependencies()
        ArrayList<String> schemaNames = new ArrayList<>()
        beans.each {KrObjectDependenciesBean bean -> schemaNames.add(bean.objectOwnerAttr().value())}

        // generate find object list sql
        CheckScriptsGenerator generator = new CheckScriptsGenerator(context)
        generator.generateErdExistence(schemaNames)

        runner.databaseFinalize()
    }

}
