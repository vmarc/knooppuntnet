package kpn.core.tools.translations

object TranslationReportToolOptions {

  def parse(args: Array[String]): Option[TranslationReportToolOptions] = {
    optionParser.parse(args, TranslationReportToolOptions())
  }

  private def optionParser: scopt.OptionParser[TranslationReportToolOptions] = {
    new scopt.OptionParser[TranslationReportToolOptions]("TranslationReportTool") {
      opt[String]('f', "file").required() valueName "<filename>" action { (x, c) =>
        c.copy(filename = x)
      } text "knooppuntnet translations filename"
    }
  }
}

case class TranslationReportToolOptions(
  filename: String = ""
)
