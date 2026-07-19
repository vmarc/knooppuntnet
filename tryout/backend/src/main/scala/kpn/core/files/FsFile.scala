package kpn.core.files

import java.io.File

trait FsFile {

  def isFile: Boolean

  def isDirectory: Boolean

  def name: String

  def toFile: File
}
