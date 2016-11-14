package com.pramati.crawler.utils

import java.io._

import org.apache.log4j.Logger

object FileIOHelper {
  private val logger: Logger = Logger.getLogger(FileIOHelper.getClass)


  def writeFileToDisk(fileName: String, data: String):Unit= {
    val writer = new PrintWriter( new FileOutputStream(fileName))
    writer.write("Hello Scala")
    writer.close()
  }

}
