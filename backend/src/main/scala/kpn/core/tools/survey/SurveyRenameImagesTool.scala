package kpn.core.tools.survey

import kpn.core.util.Log
import kpn.database.base.Options
import kpn.database.base.Tool

import java.io.File
import java.io.FilenameFilter

object SurveyRenameImagesTool extends Tool[SurveyRenameImagesToolOptions] {
  private val log = Log(classOf[SurveyRenameImagesTool])

  override def options: Options[SurveyRenameImagesToolOptions] = SurveyRenameImagesToolOptions

  override def execute(options: SurveyRenameImagesToolOptions): Unit = {
    new SurveyRenameImagesTool(new File(options.directory)).rename()
  }
}

class SurveyRenameImagesTool(dir: File) {
  def rename(): Unit = {
    val imageFiles = listImageFiles()
    imageFiles.zipWithIndex.foreach { case (imageFile, index) =>
      val newFile = new File(dir, f"${index + 1}%03d.jpg")
      imageFile.renameTo(newFile)
    }
  }

  private def listImageFiles(): Seq[File] = {
    dir.listFiles(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = {
        name.toLowerCase.endsWith(".jpg")
      }
    }).sortBy(_.getName).toSeq
  }
}
