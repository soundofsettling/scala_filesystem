package com.rtjvm.scala.oop.filesystem

import com.rtjvm.scala.oop.commands.Command
import com.rtjvm.scala.oop.files.Directory

import java.util.Scanner

object FileSystem extends App {

  val root = Directory.ROOT
  var state = State(root, root)   // ugly. using var just to make this stateful without
                                  // using full functional programming
  val scanner = new Scanner(System.in)

  while (true) {
    state.show
    val input = scanner.nextLine()
    state = Command.from(input).apply(state)
  }

}
