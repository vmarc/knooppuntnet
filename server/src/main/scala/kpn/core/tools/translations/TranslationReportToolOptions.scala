package kpn.core.tools.translations

object TranslationReportToolOptions {

  def parse(args: Array[String]): Option[TranslationReportToolOptions] = {
    optionParser.parse(args, TranslationReportToolOptions())
  }

  private def optionParser: scopt.OptionParser[TranslationReportToolOptions] = {
    new scopt.OptionParser[TranslationReportToolOptions]("TranslationReportTool") {
      opt[String]('r', "root") required() valueName "<directory>" action { (x, c) =>
        c.copy(root = x)
      } text "knooppuntnet client source root directory name"
    }
  }
}

case class TranslationReportToolOptions(
  root: String = ""
)
