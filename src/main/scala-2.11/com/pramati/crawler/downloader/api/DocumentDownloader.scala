package com.pramati.crawler.downloader.api

import com.pramati.crawler.exceptions.BusinesssException

trait DocumentDownloader {
  @throws[BusinesssException]
  def download(source: String): DocumentContainer
}
