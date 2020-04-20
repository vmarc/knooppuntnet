package kpn.core.tools.operation

import java.io.File
import java.io.PrintWriter

import jline.console.ConsoleReader
import jline.console.completer.AggregateCompleter
import jline.console.completer.ArgumentCompleter
import jline.console.completer.StringsCompleter

object OperationTool {
  def main(args: Array[String]): Unit = {
    new OperationTool().launch()
  }
}

class OperationTool {

  def launch(): Unit = {
    while (loop()) {}
  }

  private val actions = new OperationActions()

  private val reader: ConsoleReader = {
    val processes = new StringsCompleter(
      "main-dispatcher",
      "areas-dispatcher",
      "replicator",
      "updater",
      "analyzer1",
      "analyzer2",
      "analyzer3",
      "server",
      "change-set-info-tool",
      "change-set-info-tool-2",
    )
    new ConsoleReader {
      setPrompt("kpn> ")
      addCompleter(
        new AggregateCompleter(
          new StringsCompleter("status", "clear", "exit"),
          new ArgumentCompleter(new StringsCompleter("start"), processes),
          new ArgumentCompleter(new StringsCompleter("stop"), processes)
        )
      )
      val historyFile = new File(new File(System.getProperty("user.home")), ".kpn_history")
      setHistoryEnabled(true)
      val history = new AutoflushingFileHistory(historyFile)
      setHistory(history)
    }
  }

  private val out = new PrintWriter(reader.getOutput)

  private def loop(): Boolean = {
    reader.readLine.trim match {
      case "exit" => false
      case "" => true
      case "clear" =>
        reader.clearScreen
        true
      case line =>
        out.println(process(line))
        true
    }
  }

  private def process(line: String): String = {
    line match {
      case "start" => "start what?"
      case "stop" => "stop what?"
      case "status" => actions.status()
      case "start main-dispatcher" => actions.startMainDispatcher()
      case "start areas-dispatcher" => actions.startAreasDispatcher()
      case "start replicator" => actions.startReplicator()
      case "start updater" => actions.startUpdater()
      case "start analyzer1" => actions.startAnalyzer1()
      case "start analyzer2" => actions.startAnalyzer2()
      case "start analyzer3" => actions.startAnalyzer3()
      case "start server" => actions.startServer()
      case "start change-set-info-tool" => actions.startChangeSetInfoTool()
      case "start change-set-info-tool-2" => actions.startChangeSetInfoTool2()
      case "stop main-dispatcher" => actions.stopMainDispatcher()
      case "stop areas-dispatcher" => actions.stopAreasDispatcher()
      case "stop replicator" => actions.stopReplicator()
      case "stop updater" => actions.stopUpdater()
      case "stop analyzer1" => actions.stopAnalyzer1()
      case "stop analyzer2" => actions.stopAnalyzer2()
      case "stop analyzer3" => actions.stopAnalyzer3()
      case "stop server" => actions.stopServer()
      case "stop change-set-info-tool" => actions.stopChangeSetInfoTool()
      case "stop change-set-info-tool-2" => actions.stopChangeSetInfoTool2()
      case _ => "Unknown command"
    }
  }
}
