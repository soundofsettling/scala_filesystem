package com.rtjvm.scala.oop.filesystem

import com.rtjvm.scala.oop.commands.Command
import com.rtjvm.scala.oop.files.Directory

object FileSystem extends App {

  val root = Directory.ROOT
  val initialState = State(root, root)
  initialState.show

  io.Source.stdin.getLines().foldLeft(initialState)((currState, newLine) => {
    val newState = Command.from(newLine)(currState)
    newState.show
    newState
  })

}
