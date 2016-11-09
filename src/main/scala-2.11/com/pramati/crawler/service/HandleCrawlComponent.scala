package com.pramati.crawler.service

import com.pramati.crawler.downloader.api.DocumentDownloaderComponent
import com.pramati.crawler.service.facade.HandleCrawlFacadeComponent

trait HandleCrawlComponent {
  val handle : HandleCrawlApi

}
