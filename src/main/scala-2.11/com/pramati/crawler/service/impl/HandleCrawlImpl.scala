package com.pramati.crawler.service.impl

import java.util.Date

import akka.actor.ActorSystem
import com.pramati.crawler.downloader.api.DocumentDownloaderComponent
import com.pramati.crawler.model.DocumentContainer
import com.pramati.crawler.service.HandleCrawlApi
import com.pramati.crawler.service.actors.{DownloadMsgActor, SaveMsgActor}
import com.pramati.crawler.service.facade.HandleCrawlFacadeComponent
import org.apache.log4j.Logger
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class HandleCrawlImpl extends HandleCrawlApi{
  this:DocumentDownloaderComponent with HandleCrawlFacadeComponent =>

  private val baseURL: String = "http://mail-archives.apache.org"
  private val URL: String = "mod_mbox/maven-users/"
  private val threadPoolSize: Int = 100
  private val logger: Logger = Logger.getLogger(classOf[HandleCrawlImpl])
  private val system = ActorSystem("saveAndDownload")
  private val saveRef = SaveMsgActor.createSaveMsgActorPool(system)
  private val downloadRef = DownloadMsgActor.createDownloadMsgActor(system)

  def parseDocument() {
    val date: Option[Date] = handleCrawlFacade.getDateFromUser
    date match {
      case Some(date) => downloadMessageforMonthAndYear(date)
      case None=> logger.error("No date was received from user")
    }
  }

  def downloadMessageforMonthAndYear(date: Date) {
    val doc: DocumentContainer = documentDownloader.download(baseURL + "/" + URL)
    var msgURL: String = handleCrawlFacade.parseMessagesLinkForDateFromDoc(date, doc)
    msgURL = baseURL + "/" + URL + msgURL
    downloadAndSaveMsgsFromPageURL(msgURL)
    System.out.println("done")
  }

  private def downloadAndSaveMsgsFromPageURL(msgURL: String) {
    System.out.println("downloading msgs from : " + msgURL)
    val doc: DocumentContainer = documentDownloader.download(msgURL)
    val elements: Elements = handleCrawlFacade.extractElementsFromDoc(doc)

    val l = elements.toArray()
    for (e <- l)
      downloadRef ! new DownloadMsgActor.DownloadMsg(e.asInstanceOf[Element],msgURL,saveRef,new SaveMsgActor.SaveMsg(None))

    parseIfNextPageExists(doc)
  }

  private def parseIfNextPageExists(doc: DocumentContainer) {
    val nextUrlElement: Elements = doc.doc.select("a[href]:contains(Next)")
    var nextPageUrl: String = baseURL
    if (!nextUrlElement.isEmpty) {
      nextPageUrl += nextUrlElement.first.attr("href")
      downloadAndSaveMsgsFromPageURL(nextPageUrl)
    }
  }

}
