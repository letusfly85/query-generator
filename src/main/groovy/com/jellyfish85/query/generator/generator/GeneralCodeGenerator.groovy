package com.jellyfish85.query.generator.generator

import com.jellyfish85.xlsaccessor.utils.XlsAppProp

/**
 * generate general code sql loader, and sql maintenance scripts for release
 *
 *
 * @author wada shunsuke
 * @since  2013/12/16
 *
 */
class GeneralCodeGenerator {

    private XlsAppProp xlsProp = new XlsAppProp()

    public void generateDataFile() {


        xlsProp.generalCodeBookParentPath()
    }

}
