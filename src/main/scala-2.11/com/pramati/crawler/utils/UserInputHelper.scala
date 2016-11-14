package com.pramati.crawler.utils

object UserInputHelper {
  def inputFromConsole: String = {
    io.Source.stdin.bufferedReader().readLine()
  }
}
