package kpn.core.overpass

import kpn.core.util.Log

import java.io.BufferedInputStream
import java.io.InputStream
import java.io.PrintStream
import java.util.Scanner
import java.util.concurrent.CountDownLatch
import scala.collection.mutable.ListBuffer

/**
 * Executes Overpass queries using the 'osm3s_query' command. This can only be used when
 * running on the same machine as the Overpass database. The alternative is to use OverpassQueryExecutorImpl
 * which performs http requests to a remote Overpass database.
 */
class OverpassQueryExecutorImpl() extends OverpassQueryExecutor {

  private val log = Log(classOf[OverpassQueryExecutorImpl])

  def execute(queryString: String): String = {
    log.trace(s"$queryString")
    (1 to 3).foreach { attempt =>
      doExecute(attempt, queryString) match {
        case Some(result) => return result
        case None =>
      }
    }
    val message = s"Query timed out after 3 attempts\n$queryString)"
    log.warn(message)
    throw new OverpassException(message)
  }

  private def doExecute(attempt: Int, queryString: String): Option[String] = {

    val processBuilder = new java.lang.ProcessBuilder("/kpn/overpass/bin/osm3s_query", "--progress", "--verbose" /*, "--db-dir=/kpn/database"*/)
    val process = processBuilder.start()
    val pid = process.pid().toString

    log.trace(s"pid=$pid attempt=$attempt $queryString")

    val outputEnd = new CountDownLatch(2)
    val stdout = output(pid, "STDOUT", outputEnd, process.getInputStream)
    val stderr = output(pid, "STDERR", outputEnd, process.getErrorStream)

    log.trace(s"pid=$pid attempt=$attempt input start")

    issueRequest(process, queryString)

    log.trace(s"pid=$pid attempt=$attempt input end")

    val t1 = System.currentTimeMillis()
    while ( {
      val finished = process.waitFor(5000L, java.util.concurrent.TimeUnit.MILLISECONDS)
      val t2 = System.currentTimeMillis()
      if (finished) {
        // wait until the output subprocesses have stopped
        if (outputEnd.await(1000L, java.util.concurrent.TimeUnit.MILLISECONDS)) {
          log.trace(s"pid=$pid attempt=$attempt output threads closed within timeout period (${t2 - t1}ms)")
        }
        else {
          log.debug(s"pid=$pid attempt=$attempt output threads not closed after timeout (${t2 - t1}ms)")
        }
        log.debug(s"pid=$pid attempt=$attempt exit=${process.exitValue()} (${t2 - t1}ms) $queryString")
      }
      else {
        log.trace(s"pid=$pid attempt=$attempt not finished yet (${t2 - t1}ms)")
      }
      !finished
    }) {}

    val exitCode = process.exitValue()

    if (exitCode == 137) {
      val out = stdout.output.mkString("\n")
      val err = stderr.output.mkString("\n")
      val end = System.currentTimeMillis()
      log.warn(s"""pid=$pid attempt=$attempt exit=137 (after ${end - t1}ms) - will retry\n$queryString\n---out--\n$out\n---err\n$err\n---""")
      None
    }
    else if (stderr.output.exists(isTimeout)) {
      val out = stdout.output.mkString("\n")
      val err = stderr.output.mkString("\n")
      val end = System.currentTimeMillis()

      log.warn(s"""pid=$pid attempt=$attempt timeout (after ${end - t1}ms) in query response (exit=$exitCode)\n$queryString\n---out--\n$out\n---err\n$err\n---""")
      None
    }
    else {
      val errorsWithoutProgress = stderr.output.filterNot(isProgress).filter(_.trim.nonEmpty)

      if (exitCode != 0 /*|| errorsWithoutProgress.nonEmpty*/ ) {
        val errorMessage = if (errorsWithoutProgress.nonEmpty) {
          errorsWithoutProgress.mkString("\n  ", "\n  ", "")
        }
        else {
          ""
        }

        val out = stdout.output.mkString("\n")
        val err = stderr.output.mkString("\n")
        val end = System.currentTimeMillis()

        val message =
          s"""pid=$pid attempt=$attempt Error(s) generated (after ${end - t1}ms) while executing query (exit=$exitCode)\n$queryString
             |\n$errorMessage\n---out---\n$out\n---err---\n$err\n---""".stripMargin
        log.warn(message)
        throw new OverpassException(message)
      }
      Some(stdout.output.mkString("\n"))
    }
  }

  private def output(pid: String, name: String, latch: CountDownLatch, inputStream: InputStream): IOThreadHandler = {
    val out = new IOThreadHandler(pid, name, latch, inputStream)
    out.start()
    out
  }

  private def issueRequest(process: Process, queryString: String): Unit = {
    val ps = new PrintStream(process.getOutputStream)
    ps.println(queryString)
    ps.close()
  }

  private def isProgress(line: String): Boolean = {
    line.contains("After") && line.contains("in") && line.contains("part")
  }

  private def isTimeout(line: String): Boolean = {
    line.contains("Dispatcher_Client::request_read_and_idx::timeout") ||
      line.contains("runtime error: Query timed out")
  }
}

class IOThreadHandler(pid: String, name: String, latch: CountDownLatch, inputStream: InputStream) extends Thread {

  private val log = Log(classOf[IOThreadHandler])

  var output: Seq[String] = Seq.empty

  override def run(): Unit = {
    val out = ListBuffer[String]()
    val scanner = new Scanner(new BufferedInputStream(inputStream, 32768))
    try {
      log.debug(s"pid=$pid $name start")
      while (scanner.hasNextLine) {
        val line = scanner.nextLine()
        out += line
      }
    }
    finally {
      scanner.close()
    }
    output = out.toSeq
    log.debug(s"pid=$pid $name closed: ${output.size} lines")
    latch.countDown()
  }
}
