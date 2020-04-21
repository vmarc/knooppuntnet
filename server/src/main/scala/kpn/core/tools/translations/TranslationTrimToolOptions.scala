package kpn.core.tools.translations

object TranslationTrimToolOptions {

  def parse(args: Array[String]): Option[TranslationTrimToolOptions] = {
    optionParser.parse(args, TranslationTrimToolOptions())
  }

  private def optionParser: scopt.OptionParser[TranslationTrimToolOptions] = {
    new scopt.OptionParser[TranslationTrimToolOptions]("TranslationTrimTool") {
      opt[String]('r', "root").required() valueName "<directory>" action { (x, c) =>
        c.copy(root = x)
      } text "knooppuntnet client source root directory name"
    }
  }
}

case class TranslationTrimToolOptions(
  root: String = ""
)
