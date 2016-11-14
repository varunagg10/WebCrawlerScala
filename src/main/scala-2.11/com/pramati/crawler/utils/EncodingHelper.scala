package com.pramati.crawler.utils

import java.net.URLEncoder

import org.apache.log4j.Logger

object CustomEncodingHelper {
  private val logger: Logger = Logger.getLogger(CustomEncodingHelper.getClass)

  def encodeFileName(fileName: String, encoding: String): String = {
    val fileNameEncoded = URLEncoder.encode(fileName, encoding)
    fileNameEncoded
  }
}
