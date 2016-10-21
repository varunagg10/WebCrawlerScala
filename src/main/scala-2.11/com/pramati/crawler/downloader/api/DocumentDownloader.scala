package com.pramati.crawler.downloader.api

import com.pramati.crawler.exceptions.BusinesssException
import com.pramati.crawler.model.DocumentContainer

trait DocumentDownloader {
  @throws[BusinesssException]
  def download(source: String): DocumentContainer
}
