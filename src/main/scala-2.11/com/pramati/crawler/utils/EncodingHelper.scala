package com.pramati.crawler.utils

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

import com.pramati.crawler.exceptions.BusinesssException
import org.apache.log4j.Logger

object CustomEncodingHelper {
  private val logger: Logger = Logger.getLogger(CustomEncodingHelper.getClass)

  @throws[BusinesssException]
  def encodeFileName(fileName: String, encoding: String): String = {
    var fileNameEncoded = fileName
    try
      fileNameEncoded = URLEncoder.encode(fileName, encoding)

    catch {
      case e: UnsupportedEncodingException => {
        logger.error("Unsupported format for URL encodeing :" + encoding, e)
        throw new BusinesssException("Unsupported format for URL encodeing :" + encoding, e)
      }
    }
    fileNameEncoded
  }
}
