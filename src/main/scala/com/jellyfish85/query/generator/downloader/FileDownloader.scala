package com.jellyfish85.query.generator.downloader

import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.svnaccessor.manager.SVNManager
import com.jellyfish85.svnaccessor.getter.SVNGetFiles
import com.jellyfish85.query.generator.utils.QueryAppProp
import java.io.File
import org.apache.commons.io.FileUtils

/**
 *
 *
 */
object FileDownloader {

  val manager: SVNManager  = new SVNManager

  val getter:  SVNGetFiles[SVNRequestBean] = new SVNGetFiles

  val prop:    QueryAppProp     = new QueryAppProp

  /**
   *
   *
   * @param requestBean
   */
  def download(requestBean: SVNRequestBean): SVNRequestBean = {
    val folder: File = new File(prop.applicationWorkspacePath)
    FileUtils.forceMkdir(folder)

    getter.simpleGetFile(requestBean, folder, "")
    
    val list: List[SVNRequestBean] = getter.modifyAttribute2Current(List(requestBean))

    list.head
  }

  /**
   *
   *
   * @param requestBean
   */
  def downloadDir(requestBean: SVNRequestBean) {
    val folder: File = new File(prop.applicationWorkspacePath)
    FileUtils.forceMkdir(folder)

    def filter(x: SVNRequestBean): Boolean  =  true
    val requestBeans: List[SVNRequestBean] = getter.getSVNInfo(requestBean.path, filter)

    getter.simpleGetFiles(requestBeans, folder)
  }

}