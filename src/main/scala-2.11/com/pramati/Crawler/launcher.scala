package com.pramati.Crawler

import com.pramati.crawler.service.HandleCrawlApi
import com.pramati.crawler.service.impl.HandleCrawlImpl

object launcher {

  def main(args: Array[String]): Unit = {
    val hanleCrawl:HandleCrawlApi = new HandleCrawlImpl
    hanleCrawl.parseDocument()
  }
}
