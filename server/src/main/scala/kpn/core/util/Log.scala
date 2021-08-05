package kpn.core.util

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.ThreadContext

import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

trait Log {

  def isTraceEnabled: Boolean

  def isDebugEnabled: Boolean

  def trace(message: String): Unit

  def debug(message: String): Unit

  def debug(message: String, throwable: Throwable): Unit

  def debugElapsed[T](f: => (String, T)): T = {
    val (message, result) = elapsedMessage(f)
    debug(message)
    result
  }

  def info(message: String): Unit

  def infoElapsed[T](f: => (String, T)): T = {
    val (message, result) = elapsedMessage(f)
    info(message)
    result
  }

  def warn(message: String): Unit

  def error(message: String): Unit

  def error(message: String, throwable: Throwable): Unit

  def fatal(message: String, throwable: Throwable): Unit

  private def elapsedMessage[T](f: => (String, T)): (String, T) = {
    val t1 = System.nanoTime()
    val (message, result) = f
    val t2 = System.nanoTime()
    val elapsed = Elapsed((t2 - t1) / 1000)
    (s"$message ($elapsed)", result)
  }

}

object Log {

  def apply(name: String): Log = new Log4j(name)

  def apply(clazz: Class[_]): Log = new Log4j(clazz.getName)

  def mock: MockLog = new MockLog()

  def context[T](message: String)(f: => T): T = {
    ThreadContext.push(message)
    try {
      f
    } finally {
      ThreadContext.pop()
      ()
    }
  }

  def context[T](messages: Seq[String])(f: => T): T = {
    messages.foreach { message =>
      ThreadContext.push(message)
    }
    try {
      f
    } finally {
      messages.foreach { message =>
        ThreadContext.pop()
      }
      ()
    }
  }

  def contextMessages: Seq[String] = {
    ThreadContext.getImmutableStack.asList.asScala.toSeq
  }

  def contextString: String = {
    contextMessages.mkString("[", ", ", "]")
  }

  def contextAnd(message: String): String = {
    contextMessages.mkString("", ", ", ", ") + message
  }

  private class Log4j(name: String) extends Log {

    private val log = LogManager.getLogger(name)

    def isTraceEnabled: Boolean = log.isTraceEnabled

    def isDebugEnabled: Boolean = log.isDebugEnabled

    def trace(message: String): Unit = log.trace(message)

    def debug(message: String): Unit = log.debug(message)

    def debug(message: String, throwable: Throwable): Unit = log.debug(message, throwable)

    def info(message: String): Unit = log.info(message)

    def warn(message: String): Unit = log.warn(message)

    def error(message: String): Unit = log.error(message)

    def error(message: String, throwable: Throwable): Unit = log.error(message, throwable)

    def fatal(message: String, throwable: Throwable): Unit = log.fatal(message, throwable)

  }
}

class MockLog() extends Log {

  private val messageBuffer = ListBuffer[String]()

  def messages: Seq[String] = messageBuffer.toSeq

  def isTraceEnabled = true

  def isDebugEnabled = true

  def trace(message: String): Unit = {

    val stackMessages: Seq[String] = ThreadContext.getImmutableStack.asList().asScala.toSeq
    val stack = if (stackMessages.isEmpty) {
      ""
    }
    else {
      stackMessages.mkString("[", ", ", "] ")
    }

    messageBuffer.append("TRACE " + stack + message)
  }

  def debug(message: String): Unit = {

    val stackMessages: Seq[String] = ThreadContext.getImmutableStack.asList().asScala.toSeq
    val stack = if (stackMessages.isEmpty) {
      ""
    }
    else {
      stackMessages.mkString("[", ", ", "] ")
    }

    messageBuffer.append("DEBUG " + stack + message)
  }

  def debug(message: String, throwable: Throwable): Unit = {

    val stackMessages: Seq[String] = ThreadContext.getImmutableStack.asList().asScala.toSeq
    val stack = if (stackMessages.isEmpty) {
      ""
    }
    else {
      stackMessages.mkString("[", ", ", "] ")
    }

    messageBuffer.append("DEBUG " + stack + message)
  }

  def info(message: String): Unit = messageBuffer.append("INFO " + message)

  def warn(message: String): Unit = messageBuffer.append("WARN " + message)

  def error(message: String): Unit = messageBuffer.append("ERROR " + message)

  def error(message: String, throwable: Throwable): Unit = messageBuffer.append("ERROR " + message)

  def fatal(message: String, throwable: Throwable): Unit = messageBuffer.append("FATAL " + message)

}
