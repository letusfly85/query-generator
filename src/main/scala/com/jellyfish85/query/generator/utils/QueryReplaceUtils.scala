package com.jellyfish85.query.generator.utils

import scala.collection.JavaConversions._
import java.io._
import org.apache.commons.io.FileUtils
import org.apache.commons.lang.StringUtils

import com.jellyfish85.xlsaccessor.constant.AppConst
import java.util.regex.{Matcher, Pattern}

/**
 * == QueryReplaceUtils ==
 *
 * replace encoding, file names, folders and so on
 *
 */
@throws(classOf[IOException])
class QueryReplaceUtils(prop: QueryAppProp) {


  /**
   * == addSchemaName2ControlFile ==
   *
   * @param controlFile
   * @param schemaName
   */
  def addSchemaName2ControlFile(controlFile: File, schemaName: String) {
    val tmpFile: File = new File(prop.applicationWorkspacePath, "tmp")
    if (tmpFile.exists()) FileUtils.forceDelete(tmpFile)

    var pw: PrintWriter =
      new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpFile),"UTF-8")))

    var is: FileInputStream = new FileInputStream(controlFile)
    var in: BufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"))

    var idx = AppConst.INT_ZERO
    Stream.continually(in.readLine()).takeWhile(_ != null).foreach {buf: String =>
        if (idx < 10) {
          pw.write(buf.
            replace(("TABLE "  + prop.erdTableMasterPrefix),
                    ("TABLE "  + schemaName + "." + prop.erdTableMasterPrefix)) + "\n")

        } else {
          pw.write(buf + "\n")
        }

      idx += 1
    }
    pw.close()
    in.close()
    is.close()

    // exchange old and new one
    val ctlFilePath: String = controlFile.getPath
    FileUtils.forceDelete(controlFile)

    val newFile: File = new File(ctlFilePath)
    pw = new PrintWriter(new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(newFile),"UTF-8")))

    is = new FileInputStream(tmpFile)
    in = new BufferedReader(new InputStreamReader(is, "UTF-8"))

    idx = AppConst.INT_ZERO
    Stream.continually(in.readLine()).takeWhile(_ != null).foreach {buf: String =>
        pw.write(buf + "\n")
    }
    pw.close()
    in.close()
    is.close()
  }

  /**
   * == convertSJIS2UTF8 ==
   *
   *
   * @param file
   */
  def convertSJIS2UTF8(file: File) {
    val tmpFile: File = new File(prop.applicationWorkspacePath, "tmp")
    if (tmpFile.exists()) FileUtils.forceDelete(tmpFile)

    var pw: PrintWriter = new PrintWriter(new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(tmpFile),"UTF-8")))

    var is: FileInputStream = new FileInputStream(file)
    var br: BufferedReader = new BufferedReader(new InputStreamReader(is, "SJIS"))

    var idx = AppConst.INT_ZERO
    Stream.continually(br.readLine()).takeWhile(_ != null).foreach {str: String =>
        if (!StringUtils.isBlank(str)) pw.write(str + "\n")
    }
    pw.close()
    br.close()
    is.close()

    // exchange old and new one
    val filePath: String = file.getPath
    FileUtils.forceDelete(file)

    val newFile: File = new File(filePath)
    pw = new PrintWriter(new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(newFile),"UTF-8")))

    is = new FileInputStream(tmpFile)
    br = new BufferedReader(new InputStreamReader(is, "UTF-8"))

    idx = AppConst.INT_ZERO
    Stream.continually(br.readLine()).takeWhile(_ != null).foreach {buf: String =>
        pw.write(buf + "\n")
    }
    pw.close()
    br.close()
    is.close()
  }

  def addSchemaName2Procedure(dest: File, mainSchemaName: String, outerFaceSchemaName: String) {
    val tmpFile: File = new File(prop.applicationWorkspacePath, "tmp")
    if (tmpFile.exists()) FileUtils.forceDelete(tmpFile)

    var pw: PrintWriter = new PrintWriter(new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(tmpFile),"UTF-8")))

    var is: FileInputStream = new FileInputStream(dest)
    var in: BufferedReader  = new BufferedReader(new InputStreamReader(is, "SJIS"))

    var idx = AppConst.INT_ZERO
    var switch: Boolean = true
    var endFlg: Boolean = false

    Stream.continually(in.readLine()).takeWhile(_ != null).foreach {buf: String =>
      if (idx < 50 && !StringUtils.isBlank(buf)) {
        if (buf.toUpperCase().matches(".*" + " END " + ".*")) {
          endFlg = true
        }

        if (dest.getName.startsWith(this.prop.erdFunctionAnotherOwnerDDL)  && !endFlg) {
          pw.write(buf.toUpperCase().replace("FNC_", outerFaceSchemaName + ".FNC_") + "\n")

        } else if (dest.getName.toUpperCase().startsWith("FNC_")  && !endFlg) {
          pw.write(buf.toUpperCase().replace("FNC_", mainSchemaName + ".FNC_") + "\n")

        } else {
          pw.write(buf.toUpperCase().replace("PROCEDURE ", "PROCEDURE " + mainSchemaName  + ".") + "\n")
        }

      } else {
        pw.write(buf    + "\n")
      }

      idx += 1
    }
    pw.close()
    in.close()
    is.close()


    // exchange old and new one
    val filePath: String = dest.getPath
    FileUtils.forceDelete(dest)

    val newFile: File = new File(filePath)
    pw = new PrintWriter(new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(newFile),"UTF-8")))

    is = new FileInputStream(tmpFile)
    in = new BufferedReader(new InputStreamReader(is, "UTF-8"))

    idx = AppConst.INT_ZERO
    switch = true
    Stream.continually(in.readLine()).takeWhile(_ != null).foreach {buf: String =>
      pw.write(buf + "\n")
    }
    pw.close()
    in.close()
    is.close()
  }
}