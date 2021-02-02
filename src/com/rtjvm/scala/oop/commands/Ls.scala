package com.rtjvm.scala.oop.commands
import com.rtjvm.scala.oop.files.DirEntry
import com.rtjvm.scala.oop.filesystem.State

class Ls extends Command {

  override def apply(state: State): State = {
    val contents = state.workingDirectory.contents
    state.setMessage(formatOutput(contents))
  }

  def formatOutput(contents: List[DirEntry]): String = {
    if(contents.isEmpty) ""
    else {
      val entry = contents.head
      entry.name + "[" + entry.getType + "]" + "\n" + formatOutput(contents.tail)
    }
  }

}
