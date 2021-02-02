package com.rtjvm.scala.oop.filesystem

import com.rtjvm.scala.oop.files.Directory

class State(val root: Directory, val workingDirectory: Directory, val outputFromPreviousCommand: String) {

  def show: Unit = {
    println(outputFromPreviousCommand)
    print(State.SHELL_TOKEN)
  }

  def setMessage(message: String): State =
    State(root, workingDirectory, message)

}

object State {
  val SHELL_TOKEN = "$ "

  def apply(root: Directory, workingDirectory: Directory, outputFromPreviousCommand: String = ""): State =
    new State(root, workingDirectory, outputFromPreviousCommand)
}