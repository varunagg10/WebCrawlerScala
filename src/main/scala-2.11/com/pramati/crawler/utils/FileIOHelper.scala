package com.pramati.crawler.utils

import java.io._

import com.pramati.crawler.exceptions.BusinesssException
import org.apache.log4j.Logger

object FileIOHelper {
  private val logger: Logger = Logger.getLogger(FileIOHelper.getClass)

  @throws[BusinesssException]
  def writeFileToDisk(fileName: String, data: String):Unit= {
    val writer = new PrintWriter( new FileOutputStream(fileName))
    writer.write("Hello Scala")
    writer.close()
//    var outputStream: FileOutputStream = null
//    logger.debug("writing file :" + fileName)
//    try
//      outputStream = new FileOutputStream(fileName)
//      outputStream.write(data.getBytes)
//
//    catch {
//      case e: FileNotFoundException => {
//        logger.error("Error while opening output stream to file", e)
//        throw new BusinesssException("Error while opening output stream to file", e)
//      }
//      case e: IOException => {
//        logger.error("Error while writing to file", e)
//        throw new BusinesssException("Error while writing to file", e)
//      }
//    } finally try
//      outputStream.close()
//
//    catch {
//      case e: IOException => {
//        logger.error("Error closing Outout steam")
//      }
//    }
//    logger.debug("writing file:STAUS:SUCCESS :" + fileName)
  }

}
