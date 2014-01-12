package com.jellyfish85.query.generator.runner

import com.jellyfish85.query.generator.BaseContext
import org.apache.commons.io.FileUtils

class SqlLoaderRunner {

    /*
    public static void main(String[] args) {

        def dependencyGrpCd = args[0]
        def environment     = args[1]

        String workspace    = args[2]
        String scriptName   = args[3]

        BaseRunner  runner  = new BaseRunner(dependencyGrpCd, environment)
        BaseContext context = runner._context
        def queryProp = context.queryProp

        String path = "${workspace}\\${scriptName}"
        println path
        File file  = new File(path)
        String script = FileUtils.readFileToString(file)
        script = script.replace("\$PASSWORD", queryProp.erdSchemaAdminPass() + "@" + queryProp.erdSidName())
        script = "cd ${workspace}\n" + script

        String _path = "tmp.bat"
        File _file  = new File(_path)
        FileUtils.writeStringToFile(_file, script)

        String command  = "cmd /c ${_path}"

        println(command)

        Process process = command.execute()
        int result = process.waitFor()

        if (result.equals(0)) {
            return

        } else {
            throw new RuntimeException()
        }
    }
    */

    public void msWinExec(BaseRunner  runner, String scriptPath) {
        BaseContext context = runner._context
        def queryProp = context.queryProp

        println scriptPath
        File file  = new File(scriptPath)
        String script = FileUtils.readFileToString(file)
        script = script.replace("\$PASSWORD",
                (queryProp.erdSchemaAdminPass() + "@" + queryProp.erdSidName()))
        String workspace = file.getParent()
        script = "cd ${workspace}\n" + script

        String _path = "tmp.bat"
        File _file  = new File(_path)
        FileUtils.writeStringToFile(_file, script)

        String command  = "cmd /c ${_path}"

        println(command)

        Process process = command.execute()
        int result = process.waitFor()

        if (result.equals(0)) {
            return

        } else {
            throw new RuntimeException()
        }
    }
}
