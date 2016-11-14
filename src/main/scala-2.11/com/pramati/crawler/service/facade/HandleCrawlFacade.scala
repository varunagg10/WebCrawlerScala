package com.pramati.crawler.service.facade

import java.util.Date

import com.pramati.crawler.model.{DocumentContainer, MessageContainer}
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

trait HandleCrawlFacade {

  def getDateFromUser: Option[Date]

  def parseStringToDate(inputDate: String): Date

  def parseMessagesLinkForDateFromDoc(date: Date, docCon: DocumentContainer): String

  def extractMessagesFromDoc(msgURL: String, element: Element): MessageContainer

  def extractElementsFromDoc(doc: DocumentContainer): Elements

  def writeMsgToFile(messageContainer: MessageContainer)

  def validateInput(input: String): Boolean
}
