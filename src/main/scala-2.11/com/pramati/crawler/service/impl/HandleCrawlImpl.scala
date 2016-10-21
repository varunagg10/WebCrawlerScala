package com.pramati.crawler.service.impl

import java.util.Date
import java.util.concurrent.{ExecutorService, Executors}
import javax.annotation.PostConstruct

import com.pramati.crawler.downloader.api.DocumentDownloader
import com.pramati.crawler.downloader.impl.WebPageDownloadImpl
import com.pramati.crawler.exceptions.BusinesssException
import com.pramati.crawler.service.HandleCrawlApi
import com.pramati.crawler.service.facade.HandleCrawlFacade
import com.pramati.crawler.service.facade.impl.HandleCrawlFacadeImpl
import org.apache.log4j.Logger
import org.jsoup.select.Elements


class HandleCrawlImpl extends HandleCrawlApi{
  private val baseURL: String = "http://mail-archives.apache.org"
  private val URL: String = "mod_mbox/maven-users/"
  private val threadPoolSize: Int = 100
  private val documentDownloader: DocumentDownloader = new WebPageDownloadImpl
  private val handleCrawlFacade: HandleCrawlFacade = new HandleCrawlFacadeImpl
  private val logger: Logger = Logger.getLogger(classOf[HandleCrawlImpl])


  private var es: ExecutorService = null

  @PostConstruct private def initExecuters() {
    es = Executors.newFixedThreadPool(threadPoolSize)
  }

  @throws[BusinesssException]
  def parseDocument() {
    val date: Date = handleCrawlFacade.getDateFromUser
    downloadMessageforMonthAndYear(date)
  }

  @throws[BusinesssException]
  def downloadMessageforMonthAndYear(date: Date) {
    val doc: DocumentContainer = documentDownloader.download(baseURL + "/" + URL)
    var msgURL: String = handleCrawlFacade.parseMessagesLinkForDateFromDoc(date, doc)
    msgURL = baseURL + "/" + URL + msgURL
    downloadAndSaveMsgsFromPageURL(msgURL)
    es.shutdown() //wont shutdown in a live application
    System.out.println("done")
  }

  @throws[BusinesssException]
  private def downloadAndSaveMsgsFromPageURL(msgURL: String) {
    System.out.println("downloading msgs from : " + msgURL)
    val doc: DocumentContainer = documentDownloader.download(msgURL)
    val elements: Elements = handleCrawlFacade.extractElementsFromDoc(doc)
    import scala.collection.JavaConversions._
    for (e <- elements) {
      es.submit(new DownloadAndSaveMsgJob(msgURL, e, handleCrawlFacade))
    }
    parseIfNextPageExists(doc)
  }

  @throws[BusinesssException]
  private def parseIfNextPageExists(doc: DocumentContainer) {
    val nextUrlElement: Elements = doc.getDoc.select("a[href]:contains(Next)")
    var nextPageUrl: String = baseURL
    if (!nextUrlElement.isEmpty) {
      nextPageUrl += nextUrlElement.first.attr("href")
      downloadAndSaveMsgsFromPageURL(nextPageUrl)
    }
  }

}
