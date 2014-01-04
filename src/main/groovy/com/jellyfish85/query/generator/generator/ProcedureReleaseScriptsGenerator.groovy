package com.jellyfish85.query.generator.generator

import com.jellyfish85.query.generator.BaseContext
import com.jellyfish85.query.generator.utils.QueryReplaceUtils
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.apache.commons.io.FileUtils

/**
 * generate stored procedure release ddl and shell scripts
 *
 *
 * @author wada shunsuke
 * @since  2014/01/04
 *
 */
class ProcedureReleaseScriptsGenerator extends GeneralGenerator {

    private BaseContext context                    = null
    private ArrayList<SVNRequestBean> requestBeans = null
    private QueryReplaceUtils         replaceUtils = null
    public ProcedureReleaseScriptsGenerator(BaseContext _context, ArrayList<SVNRequestBean> _requestBeans) {
        super(_context)
        this.context = super.getBaseContext()

        this.requestBeans = _requestBeans
        this.replaceUtils = new QueryReplaceUtils(this.context.queryProp)
    }

    public void generateDDL() {
        String mainSchemaName      =
                this.context.tableNameHelper.requestMainSchemaName(this.context.dependentGrpCd)
        String outerFaceSchemaName =
                this.context.tableNameHelper.requestOuterFaceSchemaName(this.context.dependentGrpCd)

        this.requestBeans.each {SVNRequestBean requestBean ->
            File src  = new File(this.context.queryProp.applicationWorkspacePath(), requestBean.path())
            File dest = new File(this.context.queryProp.procedureDDLFolder(), requestBean.fileName())

            FileUtils.copyFile(src, dest)

            this.replaceUtils.convertSJIS2UTF8(dest)
            this.replaceUtils.addSchemaName2Procedure(dest, mainSchemaName, outerFaceSchemaName)
        }
    }


}
