package com.pramati.crawler.utils

import java.io.InputStreamReader
import java.util.Scanner

object UserInputHelper {
  def inputFromConsole: String = {
    //val sc: Scanner = new Scanner(new InputStreamReader(System.in))
    //val input: String = sc.next
    //input
    io.Source.stdin.bufferedReader().readLine()
  }
}
