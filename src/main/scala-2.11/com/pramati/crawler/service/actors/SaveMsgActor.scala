package com.pramati.crawler.service.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.actor.Actor.Receive
import akka.routing.RoundRobinRouter
import com.pramati.crawler.model.MessageContainer
import com.pramati.crawler.service.facade.HandleCrawlFacade
import com.pramati.crawler.service.facade.impl.HandleCrawlFacadeImpl

class SaveMsgActor extends Actor{

  val handleCrawlFacade:HandleCrawlFacade = new HandleCrawlFacadeImpl


  override def receive: Receive = {
    case SaveMsgActor.SaveMsg(msg) =>
      handleCrawlFacade.writeMsgToFile(msg)
  }
}

object SaveMsgActor{
  val threads:Int = 10

  case class SaveMsg(msg:MessageContainer)

  def createSaveMsgActorPool(system:ActorSystem): ActorRef ={
    system.actorOf(Props[SaveMsgActor].withRouter(RoundRobinRouter(threads)), name = "SaveMsgActor")
  }
}
