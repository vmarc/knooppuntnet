package kpn.core.tools.support

import kpn.core.common.Time
import kpn.core.util.Elapsed

import java.io.IOException
import java.lang.ProcessBuilder.Redirect

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
    val elapsed = Elapsed(end - start)
    val subject = (if (success) "Done: " else "Error: ") + name
    val text = s"$command\n$elapsed"
    mailSender.send(subject, text)

    println(Time.now.yyyymmddhhmm + " Done")
    println(s"Elapsed: $elapsed")
  }

  private def executeCommand(command: String): Int = {
    val processBuilder = new java.lang.ProcessBuilder("bash", "-c", command)
    processBuilder.redirectOutput(Redirect.INHERIT)
    processBuilder.redirectError(Redirect.INHERIT)
    val process = processBuilder.start
    process.waitFor()
  }
}
