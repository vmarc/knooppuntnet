package kpn.database.base

import kpn.core.util.Log

abstract class Tool[T] {

  private val log = Log(classOf[ToolLog])

  def main(args: Array[String]): Unit = {
    val exitCode = executeWithArgs(args)
    System.exit(exitCode)
  }

  private def executeWithArgs(args: Array[String]): Int = {
    try {
      options.parse(args) match {
        case Some(options) =>
          execute(options)
          Exit.Success
        case None =>
          // arguments are bad, error message will have been displayed
          Exit.Failure
      }
    } catch {
      case e: Exception =>
        log.error(e.getMessage, e)
        Exit.Failure
    }
  }

  def options: Options[T]

  def execute(options: T): Unit
}

class ToolLog
