package com.pramati.crawler

import com.pramati.crawler.downloader.api.DocumentDownloaderComponent
import com.pramati.crawler.downloader.impl.WebPageDownloadImpl
import com.pramati.crawler.service.facade.HandleCrawlFacadeComponent
import com.pramati.crawler.service.facade.impl.HandleCrawlFacadeImpl
import com.pramati.crawler.service.impl.HandleCrawlImpl
import com.pramati.crawler.service.{HandleCrawlApi, HandleCrawlComponent}

object Launcher extends HandleCrawlComponent{

  def main(args: Array[String]): Unit = {
    handle.parseDocument()
  }

  override val handle: HandleCrawlApi = new HandleCrawlImpl with DefaultHandleCrawlFacadeComponent
}

trait DefaultHandleCrawlFacadeComponent extends HandleCrawlFacadeComponent with DocumentDownloaderComponent{

  val handleCrawlFacade = new HandleCrawlFacadeImpl
  val documentDownloader = new WebPageDownloadImpl
}