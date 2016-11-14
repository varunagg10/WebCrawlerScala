package com.pramati.crawler.service.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.routing.RoundRobinRouter
import com.pramati.crawler.model.MessageContainer
import com.pramati.crawler.service.facade.HandleCrawlFacade
import com.pramati.crawler.service.facade.impl.HandleCrawlFacadeImpl

class SaveMsgActor extends Actor{

  val handleCrawlFacade:HandleCrawlFacade = new HandleCrawlFacadeImpl


  override def receive: Receive = {
    case message:SaveMsgActor.SaveMsg =>
      handleCrawlFacade.writeMsgToFile(message.getMessage())
  }
}

object SaveMsgActor{
  val threads:Int = 10

  case class SaveMsg(m:Option[NextMessage]) extends NextMessage {
    var msg:MessageContainer = _

    override def setMessage(x: Any): Unit = {
      msg=x.asInstanceOf[MessageContainer]
    }

    override def getMessage(): MessageContainer = {
      msg
    }
  }

  def createSaveMsgActorPool(system:ActorSystem): ActorRef ={
    system.actorOf(Props[SaveMsgActor].withRouter(RoundRobinRouter(threads)), name = "SaveMsgActor")
  }
}
