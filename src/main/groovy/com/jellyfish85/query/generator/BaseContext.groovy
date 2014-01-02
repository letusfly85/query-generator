package com.jellyfish85.query.generator

import com.jellyfish85.query.generator.helper.AppFileNameHelper
import com.jellyfish85.query.generator.helper.ArgumentsHelper
import com.jellyfish85.query.generator.helper.TableNameHelper
import com.jellyfish85.query.generator.utils.QueryAppProp

/**
 *
 *
 */
class BaseContext {

    public ArgumentsHelper argsHelper  = new ArgumentsHelper()

    public QueryAppProp      queryProp       = null
    public TableNameHelper   tableNameHelper = null
    public AppFileNameHelper fileNameHelper  = null

    public String dependentGrpCd             = null

    public BaseContext(String _dependentGrpCd, String environment) {
        this.dependentGrpCd  = _dependentGrpCd

        this.queryProp       = new QueryAppProp(environment)
        this.tableNameHelper = new TableNameHelper(this.queryProp)
        this.fileNameHelper  = new AppFileNameHelper(this.queryProp)
    }

}
