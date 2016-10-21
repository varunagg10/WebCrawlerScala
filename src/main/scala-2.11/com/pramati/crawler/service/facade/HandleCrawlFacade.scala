package com.pramati.crawler.service.facade

import java.util.Date

import com.pramati.crawler.exceptions.BusinesssException
import com.pramati.crawler.model.{DocumentContainer, MessageContainer}
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

trait HandleCrawlFacade {
  @throws[BusinesssException]
  def getDateFromUser: Date

  @throws[BusinesssException]
  def parseStringToDate(inputDate: String): Date

  @throws[BusinesssException]
  def parseMessagesLinkForDateFromDoc(date: Date, docCon: DocumentContainer): String

  @throws[BusinesssException]
  def extractMessagesFromDoc(msgURL: String, element: Element): MessageContainer

  @throws[BusinesssException]
  def extractElementsFromDoc(doc: DocumentContainer): Elements

  @throws[BusinesssException]
  def writeMsgToFile(messageContainer: MessageContainer)

  def validateInput(input: String): Boolean

}
