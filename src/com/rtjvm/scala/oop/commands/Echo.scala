package com.rtjvm.scala.oop.commands
import com.rtjvm.scala.oop.files.{Directory, File}
import com.rtjvm.scala.oop.filesystem.State

class Echo(args: Array[String]) extends Command {

  override def apply(state: State): State = {
    /*
    if no args, state
    if just one arg, print to console
    else if multiple args, check if the next-to-last arg is > or >>
    if > , echo to file (may create a file if it doesnt exist)
    if >> , append to file
    else , just echo everything to console
     */

    if(args.isEmpty) state
    else if (args.length == 1) state.setMessage(args(0))
    else {
      val operator = args(args.length - 2)
      val filename = args(args.length - 1)
      val contents = createContent(args, args.length - 2)

      if(operator.equals(">>"))
        doEcho(state, contents, filename, append = true)
      else if(operator.equals(">"))
        doEcho(state, contents, filename, append = false)
      else
        state.setMessage(createContent(args, args.length))
    }
  }

  def getUpdatedRootAfterEcho(currDir: Directory, path: List[String], contents: String, append: Boolean): Directory = {
    if(path.isEmpty) currDir
    else if(path.tail.isEmpty){
      val dirEntry = currDir.findEntry(path.head)

      if(dirEntry == null) currDir.addEntry(new File(currDir.path, path.head, contents))
      else if(dirEntry.isDirectory) currDir
      else
        if(append) currDir.replaceEntry(path.head, dirEntry.asFile.appendContents(contents))
        else currDir.replaceEntry(path.head, dirEntry.asFile.setContents(contents))
      // find the file to create or append to
      // if file not found, create file
      // else if entry is actually a direfctory then fail
      // else replace or append content to file
      // replace entry with the filename with the new file
    }else{
      // find next dir to navigate
      // call this fn on that
      val nextDir = currDir.findEntry(path.head).asDirectory
      val newNextDirectory = getUpdatedRootAfterEcho(nextDir, path.tail, contents, append)

      if(newNextDirectory == nextDir) currDir
      else currDir.replaceEntry(path.head, newNextDirectory)
      // if recursive call failed, fail. return currDir
      // else replace entry with the NEW dir after recursive call
    }

  }

  def doEcho(state: State, contents: String, filename: String, append: Boolean): State = {
    if(filename.contains(Directory.SEPARATOR))
      state.setMessage("Echo: filename must not contain separators!")
    else {
      val newRoot: Directory =
        getUpdatedRootAfterEcho(state.root, state.workingDirectory.getAllFoldersInPath :+ filename, contents, append)
      if(newRoot == state.root)
        state.setMessage(filename + ": no such file")
      else {
        State(newRoot, newRoot.findDescendant(state.workingDirectory.getAllFoldersInPath))
      }
    }
  }

  def createContent(args: Array[String], endIndexExclusive: Int): String = {
    (for {
     x <- 0 until endIndexExclusive
    } yield args(x)).mkString(" ")
  }

}
