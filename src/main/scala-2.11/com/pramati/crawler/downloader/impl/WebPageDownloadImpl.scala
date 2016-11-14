package com.pramati.crawler.downloader.impl

import java.io.IOException

import com.pramati.crawler.downloader.api.DocumentDownloader
import com.pramati.crawler.model.DocumentContainer
import org.apache.log4j.Logger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class WebPageDownloadImpl extends DocumentDownloader{
  private[impl] val logger: Logger = Logger.getLogger(classOf[WebPageDownloadImpl])
  private val maxDownloadAttemsts: Int = 10

  def download(source: String): DocumentContainer = {
    var doc: Document = null
    val container: DocumentContainer = new DocumentContainer
    var attpempt: Int = 0
    logger.debug("downloading from web url:" + source)
    do {
      try
        doc = Jsoup.connect(source).get
      catch {
        case e: IOException => {
          logger.error("Exception occured while downloading document form web URL " + source, e)
        }
          attpempt += 1
      }
    }while (attpempt < maxDownloadAttemsts && doc==null)

    if (attpempt == maxDownloadAttemsts) {
      logger.error("max attepts count reached from URL: " + source + " count: " + attpempt)
    }
    container.doc=doc
    container
  }
}
