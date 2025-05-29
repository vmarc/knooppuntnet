package kpn.core.tools.survey

import kpn.core.util.Log
import kpn.database.base.Exit

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
  private val log = Log(classOf[SurveyRenameImagesTool])

  def main(args: Array[String]): Unit = {
    val exitCode = execute(args)
    System.exit(exitCode)
  }

  private def execute(args: Array[String]): Int = {
    try {
      SurveyRenameImagesToolOptions.parse(args) match {
        case Some(options) => executeWithOptions(options)
        case None =>
          // arguments are bad, error message will have been displayed
          Exit.Failure
      }
    } catch {
      case e: Exception =>
        log.error(e.getMessage)
        Exit.Failure
    }
  }

  private def executeWithOptions(options: SurveyRenameImagesToolOptions): Int = {
    new SurveyRenameImagesTool(new File(options.directory)).rename()
    Exit.Success
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
