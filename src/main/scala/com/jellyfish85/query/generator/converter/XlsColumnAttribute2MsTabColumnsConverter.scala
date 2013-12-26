package com.jellyfish85.query.generator.converter

import com.jellyfish85.xlsaccessor.bean.query.generate.tool.XlsColumnAttribute
import com.jellyfish85.dbaccessor.bean.erd.mainte.tool.MsTabColumnsBean
import java.util

/**
 * == XlsColumnAttribute2MsTabColumnsConverter ==
 *
 * convert XlsColumnAttribute bean to MsTabColumns bean
 *
 * @author wada shunsuke
 * @since  2013/12/26
 *
 */
class XlsColumnAttribute2MsTabColumnsConverter {

  /**
   * == convert ==
   *
   * for bean
   *
   * @param xlsBean
   * @return
   */
  def convert(xlsBean: XlsColumnAttribute): MsTabColumnsBean = {
    val columnsBean: MsTabColumnsBean = new MsTabColumnsBean

    columnsBean.logicalTableNameAttr.setValue(xlsBean.logicalTableName)
    columnsBean.physicalTableNameAttr.setValue(xlsBean.physicalTableName)
    columnsBean.logicalColumnNameAttr.setValue(xlsBean.logicalColumnName)
    columnsBean.physicalColumnNameAttr.setValue(xlsBean.physicalColumnName)
    columnsBean.dataTypeAttr.setValue(xlsBean.dataType)
    columnsBean.dataLengthAttr.setValue(xlsBean.dataLength.toString)

    columnsBean
  }

  /**
   * == convert ==
   *
   * for list
   *
   * @param xlsBeans
   * @return
   */
  def convert(xlsBeans: util.ArrayList[XlsColumnAttribute]): util.ArrayList[MsTabColumnsBean] = {
    val columnsBeans: util.ArrayList[MsTabColumnsBean] = new util.ArrayList[MsTabColumnsBean]()

    for (i <- 0 until xlsBeans.size()) {
      val xlsBean: XlsColumnAttribute = xlsBeans.get(i)
      columnsBeans.add(convert(xlsBean))
    }

    columnsBeans
  }
}