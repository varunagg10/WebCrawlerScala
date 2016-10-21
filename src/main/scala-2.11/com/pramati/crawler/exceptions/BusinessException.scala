package com.pramati.crawler.exceptions

class BusinesssException private(ex: RuntimeException) extends RuntimeException(ex) {
  def this() = this(new RuntimeException())
  def this(message:String) = this(new RuntimeException(message))
  def this(throwable: Throwable) = this(new RuntimeException(throwable))
  def this(message:String, throwable: Throwable) = this(new RuntimeException(message, throwable))
}

object BusinesssException {
  def apply() = new BusinesssException()
  def apply(message:String) = new BusinesssException(message)
  def apply(throwable: Throwable)= new BusinesssException(throwable)
  def apply(message:String, throwable: Throwable) = new BusinesssException(message, throwable)
}
