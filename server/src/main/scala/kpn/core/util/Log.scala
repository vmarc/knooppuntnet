package kpn.core.util

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.ThreadContext

import scala.jdk.CollectionConverters._
import scala.collection.mutable.ListBuffer

trait Log {

  def isDebugEnabled: Boolean

  def isTraceEnabled: Boolean

  def info(message: String): Unit

  def warn(message: String): Unit

  def error(message: String): Unit

  def error(message: String, throwable: Throwable): Unit

  def fatal(message: String, throwable: Throwable): Unit

  def debug(message: String): Unit

  def debug(message: String, throwable: Throwable): Unit

  def debugElapsed[T](f: => (String, T)): T

  def trace(message: String): Unit

  def infoElapsed[T](message: String)(f: => T): T

  def infoElapsedSeconds[T](message: String)(f: => T): T

  def elapsed[T](f: => (String, T)): T

  def elapsedSeconds[T](f: => (String, T)): T

  def unitElapsed(f: => String): Unit

  def unitElapsedSeconds(f: => String): Unit

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

    def isDebugEnabled: Boolean = log.isDebugEnabled

    def isTraceEnabled: Boolean = log.isTraceEnabled

    def info(message: String): Unit = log.info(message)

    def warn(message: String): Unit = log.warn(message)

    def error(message: String): Unit = log.error(message)

    def error(message: String, throwable: Throwable): Unit = log.error(message, throwable)

    def fatal(message: String, throwable: Throwable): Unit = log.fatal(message, throwable)

    def debug(message: String): Unit = log.debug(message)

    def debug(message: String, throwable: Throwable): Unit = log.debug(message, throwable)

    def debugElapsed[T](f: => (String, T)): T = {
      val (message, result) = elapsedMessage(millis = true)(f)
      log.debug(message)
      result
    }

    def trace(message: String): Unit = log.trace(message)

    def infoElapsed[T](message: String)(f: => T): T = infoElapsed(message, millis = true)(f)

    def infoElapsedSeconds[T](message: String)(f: => T): T = infoElapsed(message, millis = false)(f)

    def elapsed[T](f: => (String, T)): T = elapsed(millis = true)(f)

    def elapsedSeconds[T](f: => (String, T)): T = elapsed(millis = false)(f)

    def unitElapsed(f: => String): Unit = unitElapsed(millis = true)(f)

    def unitElapsedSeconds(f: => String): Unit = unitElapsed(millis = false)(f)

    private def infoElapsed[T](message: String, millis: Boolean)(f: => T): T = {
      val t1 = System.nanoTime()
      try {
        f
      } finally {
        val t2 = System.nanoTime()
        val elapsed: Long = (t2 - t1) / (if (millis) 1000000 else 1000000000)
        val unit = if (millis) "ms" else "s"
        log.info(s"$message ($elapsed$unit)")
      }
    }

    private def debugElapsed[T](message: String, millis: Boolean)(f: => T): T = {
      val t1 = System.nanoTime()
      try {
        f
      } finally {
        val t2 = System.nanoTime()
        val elapsed: Long = (t2 - t1) / (if (millis) 1000000 else 1000000000)
        val unit = if (millis) "ms" else "s"
        log.debug(s"$message ($elapsed$unit)")
      }
    }

    private def elapsed[T](millis: Boolean)(f: => (String, T)): T = {
      val t1 = System.nanoTime()
      val (message, result) = f
      val t2 = System.nanoTime()
      val elapsed: Long = (t2 - t1) / (if (millis) 1000000 else 1000000000)
      val unit = if (millis) "ms" else "s"
      log.info(s"$message ($elapsed$unit)")
      result
    }

    private def unitElapsed(millis: Boolean)(f: => String): Unit = {
      val t1 = System.nanoTime()
      val message = f
      val t2 = System.nanoTime()
      val elapsed: Long = (t2 - t1) / (if (millis) 1000000 else 1000000000)
      val unit = if (millis) "ms" else "s"
      log.info(s"$message ($elapsed$unit)")
    }

    private def elapsedMessage[T](millis: Boolean)(f: => (String, T)): (String, T) = {
      val t1 = System.nanoTime()
      val (message, result) = f
      val t2 = System.nanoTime()
      val elapsed: Long = (t2 - t1) / (if (millis) 1000000 else 1000000000)
      val unit = if (millis) "ms" else "s"
      (s"$message ($elapsed$unit)", result)
    }
  }
}

class MockLog() extends Log {

  private val messageBuffer = ListBuffer[String]()

  def messages: Seq[String] = messageBuffer.toSeq

  def isDebugEnabled = true

  def isTraceEnabled = true

  def info(message: String): Unit = messageBuffer.append("INFO " + message)

  def warn(message: String): Unit = messageBuffer.append("WARN " + message)

  def error(message: String): Unit = messageBuffer.append("ERROR " + message)

  def error(message: String, throwable: Throwable): Unit = messageBuffer.append("ERROR " + message)

  def fatal(message: String, throwable: Throwable): Unit = messageBuffer.append("FATAL " + message)

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

  def debugElapsed[T](f: => (String, T)): T = {
    val (message, result) = f
    messageBuffer.append("DEBUG " + message)
    result
  }

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

  def infoElapsed[T](message: String)(f: => T): T = infoElapsed(message, millis = true)(f)

  def infoElapsedSeconds[T](message: String)(f: => T): T = infoElapsed(message, millis = false)(f)

  def elapsed[T](f: => (String, T)): T = elapsed(millis = true)(f)

  def elapsedSeconds[T](f: => (String, T)): T = elapsed(millis = false)(f)

  def unitElapsed(f: => String): Unit = unitElapsed(millis = true)(f)

  def unitElapsedSeconds(f: => String): Unit = unitElapsed(millis = false)(f)

  private def infoElapsed[T](message: String, millis: Boolean)(f: => T): T = {
    try {
      f
    } finally {
      messageBuffer.append("DEBUG " + message)
    }
  }

  private def debugElapsed[T](message: String, millis: Boolean)(f: => T): T = {
    try {
      f
    } finally {
      messageBuffer.append("DEBUG " + message)
    }
  }

  private def elapsed[T](millis: Boolean)(f: => (String, T)): T = {
    val (message, result) = f
    messageBuffer.append("INFO " + message)
    result
  }

  private def unitElapsed(millis: Boolean)(f: => String): Unit = {
    val message = f
    messageBuffer.append("DEBUG " + message)
  }
}
