package kpn.core.tools.survey

import kpn.database.base.Options

object SurveyRenameImagesToolOptions extends Options[SurveyRenameImagesToolOptions] {

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
