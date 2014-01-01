package com.jellyfish85.query.generator.utils

import java.io._
import org.apache.commons.io.FileUtils
import com.jellyfish85.xlsaccessor.constant.AppConst

/**
 * == QueryReplaceUtils ==
 *
 * replace encoding, file names, folders and so on
 *
 */
@throws(classOf[IOException])
class QueryReplaceUtils {

  val queryProp: QueryAppProp = new QueryAppProp

  def convertSJIS2UTF8(file: File) {
    val tmpFile: File = new File(queryProp.applicationWorkspacePath, "tmp")
    if (tmpFile.exists()) FileUtils.forceDelete(tmpFile)

    var pw: PrintWriter = new PrintWriter(new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(tmpFile),"UTF-8")))

    var is: FileInputStream = new FileInputStream(file)
    var in: BufferedReader = new BufferedReader(new InputStreamReader(is, "SJIS"))

    var idx = AppConst.INT_ZERO
    var switch: Boolean = true
    while (switch) {
      val buf = in.readLine()
      if (buf == null) {
        switch = false
      }
      else {
        pw.write(buf + "\n")
      }

      idx += 1
    }
    pw.close()
    in.close()
    is.close()

    // exchange old and new one
    val filePath: String = file.getPath
    FileUtils.forceDelete(file)

    val newFile: File = new File(filePath)
    pw = new PrintWriter(new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(newFile),"UTF-8")))

    is = new FileInputStream(tmpFile)
    in = new BufferedReader(new InputStreamReader(is, "UTF-8"))

    idx = AppConst.INT_ZERO
    switch = true
    while (switch) {
      val buf = in.readLine()
      if (buf == null) {
        switch = false

      } else {
        pw.write(buf + "\n")
      }

      idx += 1
    }
    pw.close()
    in.close()
    is.close()
  }
}