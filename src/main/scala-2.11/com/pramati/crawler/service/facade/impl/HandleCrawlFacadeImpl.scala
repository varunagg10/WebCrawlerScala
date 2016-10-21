package com.pramati.crawler.service.facade.impl

import java.text.{DateFormat, ParseException, SimpleDateFormat}
import java.util.Date
import java.util.regex.{Matcher, Pattern}

import com.pramati.crawler.downloader.api.DocumentDownloader
import com.pramati.crawler.downloader.impl.WebPageDownloadImpl
import com.pramati.crawler.exceptions.BusinesssException
import com.pramati.crawler.service.facade.HandleCrawlFacade
import com.pramati.crawler.utils.{FileIOHelper, UserInputHelper}
import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper
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

  @throws[BusinesssException]
  def parseMessagesLinkForDateFromDoc(date: Date, docCont: DocumentContainer): String = {
    val doc: Document = docCont.getDoc
    val dt: String = sdf.format(date)
    val elements: Elements = doc.select(".date").select(":contains(" + dt + ")")
    if (elements.size == 0) {
      logger.error("No records found for entered date :" + dt)
      System.out.println("No records found for entered date :" + dt)
      throw new BusinesssException("No records found for entered date :" + dt)
    }
    val nextUrl: String = elements.parents.first.select("a[href]").first.attr("href")
    nextUrl
  }

  @throws[BusinesssException]
  def extractMessagesFromDoc(msgsURL: String, element: Element): MessageContainer = {
    val messageContainer: MessageContainer = new MessageContainer
    var msgURL: String = null
    msgURL = msgsURL.split("thread")(0)
    val URL: String = msgURL + element.attr("href")
    val container: DocumentContainer = documentDownloader.download(URL)
    val document: Document = container.getDoc
    messageContainer.setDate(container.getDoc.select(".date").select(".right").text)
    messageContainer.setSubject(document.select(".subject").select(".right").text)
    messageContainer.setBody(document.select("pre").text)
    messageContainer
  }

  @throws[BusinesssException]
  def extractElementsFromDoc(docCon: DocumentContainer): Elements = {
    val messageContainers: Array[MessageContainer] = null
    val doc: Document = docCon.getDoc
    val elements: Elements = doc.select("a[href*=@]")
    elements
  }

  @throws[BusinesssException]
  def writeMsgToFile(messageContainer: MessageContainer) {
    var fileName: String = messageContainer.getSubject + ":::" + messageContainer.getDate
    fileName = filePath + "/" + EncodingHelper.encodeFileName(fileName, encoding)
    FileIOHelper.writeFileToDisk(fileName, messageContainer.getBody)
  }

  @throws[BusinesssException]
  def getDateFromUser: Date = {
    System.out.println("Please enter the month and year in mm/yyyy format between 1900 to 2199")
    var input: String = UserInputHelper.inputFromConsole
    var attempt: Int = 0

    do{
      System.out.println("Please enter the month and year in mm/yyyy format between 1900 to 2199")
      input = UserInputHelper.inputFromConsole
      attempt += 1
    }while (attempt < maxAttempts && validateInput(input))

    if (attempt == maxAttempts) {
      logger.error("User attempts exceeded max attempts,exiting")
      throw new BusinesssException("User attempts exceeded max attempts,exiting")
    }
    parseStringToDate(input)
  }

  @throws[BusinesssException]
  def parseStringToDate(inputDate: String): Date = {
    val sourceFormat: DateFormat = new SimpleDateFormat("MM/yyyy")
    var date: Date = null
    try
      date = sourceFormat.parse(inputDate)

    catch {
      case e: ParseException => {
        logger.error(e)
        throw new BusinesssException("Exception occured while parsing date " + inputDate, e)
      }
    }
    date
  }

  def validateInput(input: String): Boolean = {
    val r: Pattern = Pattern.compile(pattern)
    val m: Matcher = r.matcher(input)
    m.matches
  }
}
