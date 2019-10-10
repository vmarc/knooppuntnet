package kpn.core.tools

object ChangesToolOptions {

  def parse(args: Array[String]): Option[ChangesToolOptions] = {
    optionParser.parse(args, ChangesToolOptions())
  }

  private def optionParser: scopt.OptionParser[ChangesToolOptions] = {
    new scopt.OptionParser[ChangesToolOptions]("ChangesTool") {
      head("ChangesTool")

      opt[String]('a', "analysis") required() valueName "<analysis-database>" action { (x, c) =>
        c.copy(analysisDatabaseName = x)
      } text "analysis database name"

      opt[String]('c', "changes") required() valueName "<changes-database>" action { (x, c) =>
        c.copy(changeDatabaseName = x)
      } text "changes database name"

      opt[String]('t', "tasks") required() valueName "<tasks-database>" action { (x, c) =>
        c.copy(taskDatabaseName = x)
      } text "tasks database name"

      opt[String]('b', "begin-replication-id") required() valueName "<replication-id>" action { (x, c) =>
        c.copy(beginReplicationId = x)
      } text "replication id in 999/999/999 format from which to start reading diff files"

      opt[String]('e', "end-replication-id") required() valueName "<replication-id>" action { (x, c) =>
        c.copy(endReplicationId = x)
      } text "replication id in 999/999/999 format until which to read diff files"
    }
  }
}

case class ChangesToolOptions(
  analysisDatabaseName: String = "",
  changeDatabaseName: String = "",
  taskDatabaseName: String = "",
  beginReplicationId: String = "",
  endReplicationId: String = ""
)
