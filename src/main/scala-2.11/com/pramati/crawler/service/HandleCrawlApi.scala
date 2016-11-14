package com.pramati.crawler.service

import com.pramati.crawler.downloader.api.DocumentDownloaderComponent
import com.pramati.crawler.service.facade.HandleCrawlFacadeComponent

trait HandleCrawlApi {
  this:DocumentDownloaderComponent with HandleCrawlFacadeComponent =>

  def parseDocument():Unit
}
