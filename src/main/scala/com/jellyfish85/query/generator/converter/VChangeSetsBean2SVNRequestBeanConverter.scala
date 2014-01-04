package com.jellyfish85.query.generator.converter

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.VChangesetsBean
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import java.util

class VChangeSetsBean2SVNRequestBeanConverter {

  def convert(list: List[VChangesetsBean]): List[SVNRequestBean] = {
    var beans: List[SVNRequestBean] = List()

    list.foreach {bean: VChangesetsBean =>
      val targetBean: SVNRequestBean = new SVNRequestBean

      targetBean.fileName = bean.fileNameAttr.value
      targetBean.path     = bean.pathAttr.value
      targetBean.revision = bean.revisionAttr.value.longValue()

      beans ::= targetBean
    }

    beans
  }

  def convertA(list: List[VChangesetsBean]): util.ArrayList[SVNRequestBean] = {
    val beans: util.ArrayList[SVNRequestBean] = new util.ArrayList[SVNRequestBean]()

    list.foreach {bean: VChangesetsBean =>
      val targetBean: SVNRequestBean = new SVNRequestBean

      targetBean.fileName = bean.fileNameAttr.value
      targetBean.path     = bean.pathAttr.value
      targetBean.revision = bean.revisionAttr.value.longValue()

      beans.add(bean)
    }

    beans
  }

}
