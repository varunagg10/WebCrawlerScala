package com.pramati.crawler.service

import com.pramati.crawler.downloader.api.{DocumentDownloader, DocumentDownloaderComponent}
import com.pramati.crawler.service.facade.{HandleCrawlFacade, HandleCrawlFacadeComponent}

trait HandleCrawlApi {
  this:DocumentDownloaderComponent with HandleCrawlFacadeComponent =>

  def parseDocument():Unit

//  protected def documentDownloaderP = documentDownloader
//  protected def handleCrawlFacadeP = handleCrawlFacade

//  protected def getDocumentDownloader: DocumentDownloader = {
//    documentDownloader
//  }
//
//  protected def getHandleCrawlFacade:HandleCrawlFacade={
//    handleCrawlFacade
//  }

}
