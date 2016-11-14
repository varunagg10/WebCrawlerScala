package com.pramati.crawler.downloader.api

import com.pramati.crawler.model.DocumentContainer

trait DocumentDownloader {
  def download(source: String): DocumentContainer
}
