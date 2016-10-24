package com.pramati.crawler.service.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.routing.RoundRobinRouter
import com.pramati.crawler.model.MessageContainer
import com.pramati.crawler.service.facade.HandleCrawlFacade
import com.pramati.crawler.service.facade.impl.HandleCrawlFacadeImpl
import org.jsoup.nodes.Element

class DownloadMsgActor()  extends Actor{

  val handleCrawlFacade: HandleCrawlFacade = new HandleCrawlFacadeImpl

  override def receive: Receive = {
    case DownloadMsgActor.DownloadMsg(e,s,actorRef) =>
      var messageContainer:MessageContainer  = handleCrawlFacade.extractMessagesFromDoc(s,e)
      actorRef ! new SaveMsgActor.SaveMsg(messageContainer)
  }
}

object DownloadMsgActor{
  val threads:Int = 100

  case class DownloadMsg(element: Element,url: String,actorRef: ActorRef)

  def createDownloadMsgActor(system:ActorSystem,actorRef: ActorRef): ActorRef ={
    system.actorOf(Props[DownloadMsgActor].withRouter(RoundRobinRouter(threads)), name = "DownloadMsgActor")
  }
}
