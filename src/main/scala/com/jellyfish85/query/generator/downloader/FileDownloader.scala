package com.jellyfish85.query.generator.downloader

import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.svnaccessor.manager.SVNManager
import com.jellyfish85.svnaccessor.getter.SVNGetFiles
import com.jellyfish85.query.generator.utils.QueryAppProp
import java.io.File
import org.apache.commons.io.FileUtils
import org.tmatesoft.svn.core.io.SVNRepository

/**
 *
 *
 */
object FileDownloader {

  val manager: SVNManager = new SVNManager
  val defaultRepository: SVNRepository = manager.repository
  val getter:  SVNGetFiles[SVNRequestBean] = new SVNGetFiles

  /**
   *
   *
   * @param requestBean
   */
  def download(prop:    QueryAppProp, requestBean: SVNRequestBean): SVNRequestBean = {

    getter.reSetRepository(manager.baseUrl + prop.subversionTagHeader)

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
  def download(prop:    QueryAppProp, requestBean: SVNRequestBean, path: String): SVNRequestBean = {
    val folder: File = new File(path)
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
  def downloadDir(prop:    QueryAppProp, requestBean: SVNRequestBean) {
    val folder: File = new File(prop.applicationWorkspacePath)
    FileUtils.forceMkdir(folder)

    def filter(x: SVNRequestBean): Boolean  =  true
    val requestBeans: List[SVNRequestBean] = getter.getSVNInfo(requestBean.path, filter)

    getter.simpleGetFiles(requestBeans, folder)
  }

  /**
   *
   *
   * @param requestBean
   * @param path
   */
  def downloadDir(requestBean: SVNRequestBean, path: String) {
    val folder: File = new File(path)
    FileUtils.forceMkdir(folder)

    def filter(x: SVNRequestBean): Boolean  =  true
    val requestBeans: List[SVNRequestBean] = getter.getSVNInfo(requestBean.path, filter)

    getter.simpleGetFiles(requestBeans, folder)
  }

}
