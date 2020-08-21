package kpn.core.tools.support

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

import kpn.core.common.Elapsed
import kpn.core.common.Time

object ScriptTool {
  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Usage: ScriptTool name command")
      System.exit(-1)
    }
    val name = args(0)
    val command = args(1)
    new ScriptTool(name).execute(command)
  }
}

class ScriptTool(name: String) {

  private val mailSender = new ToolMailSender()

  def execute(command: String): Unit = {

    println(Time.now.yyyymmddhhmm + " Start")
    val start = System.currentTimeMillis()

    val success = try {
      executeCommand(command)
      true
    } catch {
      case e: IOException =>
        e.printStackTrace()
        false
    }

    val end = System.currentTimeMillis()
    val elapsed = Elapsed.string(end - start)
    val subject = (if (success) "Done: " else "Error: ") + name
    val text = s"$command\n$elapsed"
    mailSender.send(subject, text)

    println(Time.now.yyyymmddhhmm + " Done")
    println(s"Elapsed: $elapsed")
  }

  private def executeCommand(command: String): Unit = {

    val p = Runtime.getRuntime.exec(Array("bash", "-c", command))
    val inputStream = new BufferedReader(new InputStreamReader(p.getInputStream))
    val errorStream = new BufferedReader(new InputStreamReader(p.getErrorStream))

    var line = inputStream.readLine()
    while (line != null) {
      println(line)
      line = inputStream.readLine()
    }

    var errorLine = errorStream.readLine()
    while (errorLine != null) {
      println("ERROR " + errorLine)
      errorLine = inputStream.readLine()
    }
  }

}
