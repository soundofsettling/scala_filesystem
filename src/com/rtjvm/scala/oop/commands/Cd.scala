package com.rtjvm.scala.oop.commands
import com.rtjvm.scala.oop.files.{DirEntry, Directory}
import com.rtjvm.scala.oop.filesystem.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {

  override def apply(state: State): State = {
    // 1. find root
    val root = state.root
    val pwd = state.workingDirectory

    // 2. find the absolute path of the directory to cd to
    val absolutePath = {
      if(dir.startsWith(Directory.SEPARATOR)) dir
      else if(pwd.isRoot) pwd.path + dir
      else pwd.path + Directory.SEPARATOR + dir
    }

    // 3. given the path, find the directory to cd to
    val destinationDirectory = doFindEntry(root, absolutePath)

    // 4. change the state given the new directory
    if(destinationDirectory == null || !destinationDirectory.isDirectory)
      state.setMessage(dir + " : no such directory")
    else
      State(root, destinationDirectory.asDirectory)
  }

  def doFindEntry(root: Directory, path: String): DirEntry = {

    @tailrec
    def findEntryHelper(currentDirectory: Directory, path: List[String]): DirEntry = {
      if(path.isEmpty || path.head.isEmpty)
        currentDirectory
      else if(path.tail.isEmpty)
        currentDirectory.findEntry(path.head)
      else {
        val nextDir = currentDirectory.findEntry(path.head)
        if (nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }
    }

    def collapseRelativeTokens(path: List[String]): List[String] = path match {
      case List() => {
        List()
      }
      case "." :: _  => {
        collapseRelativeTokens(path.tail)
      }
      case _ :: ".." :: remainder => {
        collapseRelativeTokens(remainder)
      }
      case first :: second :: ".." :: remainder => {
        collapseRelativeTokens(List(first) ::: collapseRelativeTokens(remainder))
      }
      case _ => {
        List(path.head) ::: collapseRelativeTokens(path.tail)
      }
    }

    // 1. get tokens from path
    val tokens: List[String] = collapseRelativeTokens(path.substring(1).split(Directory.SEPARATOR).toList)

    if(tokens == null) null

    // 2. navigate to the correct entry
    findEntryHelper(root, tokens)
  }

}
