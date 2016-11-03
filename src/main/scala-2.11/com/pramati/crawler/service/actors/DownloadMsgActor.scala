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
    case DownloadMsgActor.DownloadMsg(e,s,actorRef,m) => {

      val messageContainer:MessageContainer  = handleCrawlFacade.extractMessagesFromDoc(s,e)
      m.setMessage(messageContainer)

      actorRef ! m
    }

  }
}

object DownloadMsgActor{
  val threads:Int = 100

  case class DownloadMsg(element: Element,url: String,actorRef:ActorRef,m:NextMessage) extends NextMessage {
    override def setMessage(x: Any): Unit = {

    }

    override def getMessage(): Any = {
      None
    }
  }

  def createDownloadMsgActor(system:ActorSystem): ActorRef ={
    system.actorOf(Props[DownloadMsgActor].withRouter(RoundRobinRouter(threads)), name = "DownloadMsgActor")
  }
}
