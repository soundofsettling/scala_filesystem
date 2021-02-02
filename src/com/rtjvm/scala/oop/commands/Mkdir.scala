package com.rtjvm.scala.oop.commands

import com.rtjvm.scala.oop.files.{DirEntry, Directory}
import com.rtjvm.scala.oop.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {

  override def doCreateEmptyEntry(state: State): DirEntry = {
    Directory.empty(state.workingDirectory.path, name)
  }

}
