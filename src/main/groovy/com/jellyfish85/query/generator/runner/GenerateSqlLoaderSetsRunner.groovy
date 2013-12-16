package com.jellyfish85.query.generator.runner

import com.jellyfish85.query.generator.downloader.FileDownloader
import com.jellyfish85.query.generator.utils.QueryAppProp
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.GeneralCodeXlsBean
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.GeneralXlsBean
import com.jellyfish85.xlsaccessor.dao.query.generate.tool.GeneralCodeXlsDao
import com.jellyfish85.xlsaccessor.utils.XlsAppProp
import org.apache.commons.lang.StringUtils

/**
 * == GenerateSqlLoaderSetsRunner ==
 *
 *
 * @author wada shunsuke
 * @since  2013/12/08
 * @todo
 *
 */
class GenerateSqlLoaderSetsRunner {

    public static void main(String[] args) {
        XlsAppProp   xlsProp   = new XlsAppProp()
        QueryAppProp queryProp = new QueryAppProp()

        SVNRequestBean requestBean = new SVNRequestBean()
        requestBean.setPath(xlsProp.generalCodeBookPath())

        requestBean = FileDownloader.download(requestBean)

        String bookPath =
                StringUtils.join([queryProp.applicationWorkspacePath(), requestBean.path()], "/")

        GeneralCodeXlsDao codeXlsDao = new GeneralCodeXlsDao()
        def _codeXlsBeans =
                codeXlsDao.findAll(bookPath, new BigDecimal(0), requestBean)
        ArrayList<GeneralCodeXlsBean> codeXlsBeans = codeXlsDao.convert(_codeXlsBeans)

        codeXlsBeans.each {GeneralCodeXlsBean codeXlsBean ->
            println(codeXlsBean.codeId() + "\t" + codeXlsBean.fileName() + "\t" + codeXlsBean.author())
        }
        println(codeXlsBeans.size())

    }
}