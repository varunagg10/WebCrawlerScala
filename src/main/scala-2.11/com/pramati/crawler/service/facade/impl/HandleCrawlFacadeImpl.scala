package com.pramati.crawler.service.facade.impl

import java.text.{DateFormat, SimpleDateFormat}
import java.util.Date
import java.util.regex.{Matcher, Pattern}

import com.pramati.crawler.downloader.api.DocumentDownloader
import com.pramati.crawler.downloader.impl.WebPageDownloadImpl
import com.pramati.crawler.model.{DocumentContainer, MessageContainer}
import com.pramati.crawler.service.facade.HandleCrawlFacade
import com.pramati.crawler.utils.{CustomEncodingHelper, FileIOHelper, UserInputHelper}
import org.apache.log4j.Logger
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements

class HandleCrawlFacadeImpl extends HandleCrawlFacade{
  private val logger: Logger = Logger.getLogger(classOf[HandleCrawlFacadeImpl])
  private val sdf: SimpleDateFormat = new SimpleDateFormat("MMM yyyy")
  private val filePath: String = "/home/varuna/IdeaProjects/output/webcrawler"
  private val encoding: String ="UTF-8"
  private val maxAttempts: Int = 10
  private val documentDownloader: DocumentDownloader = new WebPageDownloadImpl
  private val pattern: String = "^([1-9]|0[1-9]|1[0-2])/(19|2[0-1])\\d{2}$"

  def parseMessagesLinkForDateFromDoc(date: Date, docCont: DocumentContainer): String = {
    val doc: Document = docCont.doc
    val dt: String = sdf.format(date)
    val elements: Elements = doc.select(".date").select(":contains(" + dt + ")")
    if (elements.size == 0) {
      logger.error("No records found for entered date :" + dt)
      System.out.println("No records found for entered date :" + dt)
      throw new RuntimeException("No records found for entered date :" + dt)
    }
    val nextUrl: String = elements.parents.first.select("a[href]").first.attr("href")
    nextUrl
  }

  def extractMessagesFromDoc(msgsURL: String, element: Element): MessageContainer = {
    val messageContainer: MessageContainer = new MessageContainer
    //var msgURL: String = null
    //msgURL = msgsURL.split("thread")(0)
    val URL: String = msgsURL.split("thread")(0) + element.attr("href")
    val container: DocumentContainer = documentDownloader.download(URL)
    val document: Document = container.doc
    messageContainer.date=container.doc.select(".date").select(".right").text
    messageContainer.subject=document.select(".subject").select(".right").text
    messageContainer.body=document.select("pre").text
    messageContainer
  }

  def extractElementsFromDoc(docCon: DocumentContainer): Elements = {
    val messageContainers: Array[MessageContainer] = null
    val doc: Document = docCon.doc
    val elements: Elements = doc.select("a[href*=@]")
    elements
  }

  def writeMsgToFile(messageContainer: MessageContainer) {
    var fileName: String = messageContainer.subject + ":::" + messageContainer.date
    fileName = filePath + "/" + CustomEncodingHelper.encodeFileName(fileName, encoding)
    FileIOHelper.writeFileToDisk(fileName, messageContainer.body)
  }

  def getDateFromUser: Option[Date] = {
    var input: String = ""
    var attempt: Int = 0

    do{
      System.out.println("Please enter the month and year in mm/yyyy format between 1900 to 2199")
      input = UserInputHelper.inputFromConsole
      attempt += 1
    }while (attempt < maxAttempts && !validateInput(input))

    if (attempt == maxAttempts) {
      return None
    }
    Option(parseStringToDate(input))
  }

  def parseStringToDate(inputDate: String): Date = {
    val sourceFormat: DateFormat = new SimpleDateFormat("MM/yyyy")
    val date = sourceFormat.parse(inputDate)
    date
  }

  def validateInput(input: String): Boolean = {
    val r: Pattern = Pattern.compile(pattern)
    val m: Matcher = r.matcher(input)
    m.matches
  }
}
