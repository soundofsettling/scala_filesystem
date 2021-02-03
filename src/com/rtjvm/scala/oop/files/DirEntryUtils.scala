package com.rtjvm.scala.oop.files

object DirEntryUtils {

  def absolutePath(dir: String, workingDirectory: Directory) = {
    if(dir.startsWith(Directory.SEPARATOR)) dir
    else if(workingDirectory.isRoot) workingDirectory.path + dir
    else workingDirectory.path + Directory.SEPARATOR + dir
  }

}
