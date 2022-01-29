package kpn.core.tools.survey

import java.io.File
import java.io.FilenameFilter

object SurveyRenameImagesToolOptions {

  def parse(args: Array[String]): Option[SurveyRenameImagesToolOptions] = {
    optionParser.parse(args, SurveyRenameImagesToolOptions())
  }

  private def optionParser: scopt.OptionParser[SurveyRenameImagesToolOptions] = {
    new scopt.OptionParser[SurveyRenameImagesToolOptions]("SurveyRenameImagesTool") {
      head("SurveyRenameImagesTool")
      opt[String]('d', "directory").required() valueName "<directory>" action { (x, c) =>
        c.copy(directory = x)
      } text "the directory with the images to be renamed"
    }
  }
}

case class SurveyRenameImagesToolOptions(
  directory: String = ""
)

object SurveyRenameImagesTool {
  def main(args: Array[String]): Unit = {
    val exit = SurveyRenameImagesToolOptions.parse(args) match {
      case Some(options) =>
        new SurveyRenameImagesTool(new File(options.directory)).rename()
        0

      case None =>
        // arguments are bad, error message will have been displayed
        -1
    }
    System.exit(exit)
  }
}

class SurveyRenameImagesTool(dir: File) {
  def rename(): Unit = {
    val imageFiles = listImageFiles()
    imageFiles.zipWithIndex.foreach { case (imageFile, index) =>
      val newFile = new File(dir, "%03d.jpg".format(index + 1))
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
