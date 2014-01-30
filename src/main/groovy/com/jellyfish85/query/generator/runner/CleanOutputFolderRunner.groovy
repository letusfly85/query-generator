package com.jellyfish85.query.generator.runner

import com.jellyfish85.query.generator.BaseContext

class CleanOutputFolderRunner {

    public static void main(String[] args) {
        def dependencyGrpCd = args[0]
        def environment     = args[1]

        BaseRunner  runner  = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context = runner._context
    }

}
