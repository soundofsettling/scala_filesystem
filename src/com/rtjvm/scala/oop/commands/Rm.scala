package com.rtjvm.scala.oop.commands
import com.rtjvm.scala.oop.files.{DirEntryUtils, Directory}
import com.rtjvm.scala.oop.filesystem.State

class Rm(name: String) extends Command {

  override def apply(state: State): State = {
    // 1. get working directory
    val pwd = state.workingDirectory

    // 2. get absolute path of the object to delete
    val absolutePath = DirEntryUtils.absolutePath(name, pwd)

    // 3. do some checks e.g. forbid `rm /`
    if(absolutePath.equals(Directory.ROOT_PATH)){
      state.setMessage("Cannot rm root directory!")
    }else {
      doRm(state, absolutePath)
    }


  }

  def doRm(state: State, path: String): State = {
    // 4. find the entry to remove
    // 5. update structure like we do for mkdir

    def rmHelper(currDir: Directory, path: List[String]): Directory = {
      if(path.isEmpty) currDir
      else if(path.tail.isEmpty) currDir.removeEntry(path.head)
      else {
        val nextDir = currDir.findEntry(path.head)
        if(!nextDir.isDirectory) currDir
        else {
          val newNextDirectory = rmHelper(nextDir.asDirectory, path.tail)
          if(newNextDirectory == nextDir) currDir else currDir.replaceEntry(path.head, newNextDirectory)
        }
      }
    }

    val tokens = path.substring(1).split(Directory.SEPARATOR).toList
    val newRoot: Directory = rmHelper(state.root, tokens)

    if(newRoot == state.root)
      state.setMessage(path + ": no such file or directory")
    else
      State(newRoot, newRoot.findDescendant(state.workingDirectory.path.substring(1)))

  }

}
