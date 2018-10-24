package kpn.core.tools.backup

object BackupToolOptions {

  def parse(args: Array[String]): Option[BackupToolOptions] = {
    optionParser.parse(args, BackupToolOptions())
  }

  private def optionParser: scopt.OptionParser[BackupToolOptions] = {
    new scopt.OptionParser[BackupToolOptions]("BackupTool") {
      head("BackupTool")

      opt[String]('l', "localRoot") required() valueName "<directory>" action { (x, c) =>
        c.copy(localRoot = x)
      } text "local host root directory"

      opt[String]('r', "remoteRoot") required() valueName "<directory>" action { (x, c) =>
        c.copy(remoteRoot = x)
      } text "remote host root directory"

      opt[String]('d', "directory") required() valueName "<directory>" action { (x, c) =>
        c.copy(directory = x)
      } text "the directory to be backed up"
    }
  }
}

case class BackupToolOptions(
  localRoot: String = "",
  remoteRoot: String = "",
  directory: String = ""
)
