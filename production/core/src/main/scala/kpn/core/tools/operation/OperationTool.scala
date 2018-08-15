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

  private val reader = {
    val processes = new StringsCompleter("main-dispatcher", "areas-dispatcher", "replicator", "updater", "analyzer")
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
      case "start analyzer" => actions.startAnalyzer()
      case "stop main-dispatcher" => actions.stopMainDispatcher()
      case "stop areas-dispatcher" => actions.stopAreasDispatcher()
      case "stop replicator" => actions.stopReplicator()
      case "stop updater" => actions.stopUpdater()
      case "stop analyzer" => actions.stopAnalyzer()
      case _ => "Unknown command"
    }
  }
}
