package kpn.core.tools.log

import kpn.api.common.status.ActionTimestamp
import kpn.api.custom.Timestamp
import kpn.core.action.LogAction
import kpn.core.action.LogValue
import kpn.core.db.couch.Couch
import kpn.core.tools.log.analyzers.AnalysisAnalyzer
import kpn.core.tools.log.analyzers.ApiAnalyzer
import kpn.core.tools.log.analyzers.ApplicationAnalyzer
import kpn.core.tools.log.analyzers.AssetAnalyzer
import kpn.core.tools.log.analyzers.LogRecordAnalyzer
import kpn.core.tools.log.analyzers.RobotAnalyzer
import kpn.core.tools.log.analyzers.TileAnalyzer
import kpn.server.repository.FrontendMetricsRepository
import kpn.server.repository.FrontendMetricsRepositoryImpl
import nl.basjes.parse.core.Parser
import nl.basjes.parse.core.exceptions.DissectionFailure
import nl.basjes.parse.httpdlog.HttpdLoglineParser

import scala.annotation.tailrec
import scala.io.Source
import scala.jdk.CollectionConverters._

object LogAnalyzerTool {
  def main(args: Array[String]): Unit = {
    // new LogAnalyzerTool().printPossiblePaths()
    // new LogAnalyzerTool().findUserAgents("/kpn/logs/ningx-nl-access.log")

    Couch.executeIn("localhost", "frontend-actions") { database =>
      val repo = new FrontendMetricsRepositoryImpl(null, database, false)
      //new LogAnalyzerTool(repo).analyze("/kpn/logs/ningx-nl-access.log", "test")
      new LogAnalyzerTool(repo).analyze("/kpn/logs/ningx-be-access.log", "be")
      new LogAnalyzerTool(repo).analyze("/kpn/logs/ningx-nl-experimental-access.log", "nl-experimental")
    }
  }
}

class LogAnalyzerTool(frontendMetricsRepository: FrontendMetricsRepository) {

  private val LOG_FORMAT = "$remote_addr - $remote_user [$time_local] \"$host\" \"$request\" $status $body_bytes_sent \"$http_referer\" \"$http_user_agent\"rt=$request_time uct=\"upstream_connect_time\" uht=\"upstream_header_time\" urt=\"$upstream_response_time\""

  def printPossiblePaths(): Unit = {
    val dummyParser = new HttpdLoglineParser[Object](classOf[Object], LOG_FORMAT)
    val possiblePaths = dummyParser.getPossiblePaths.asScala
    for (path <- possiblePaths) {
      println(path)
    }
  }

  def findUserAgents(filename: String): Unit = {
    val parser = new HttpdLoglineParser[LogRecord](classOf[LogRecord], LOG_FORMAT)
    val source = Source.fromFile(filename)

    var frequency: Map[String, Int] = Map.empty
    source.getLines().foreach { line =>
      try {
        val record = parser.parse(line)
        if (record.userAgent != null) {
          frequency = frequency.updated(record.userAgent, frequency.getOrElse(record.userAgent, 0) + 1)
        }
      }
      catch {
        case e: DissectionFailure => println("ERROR: " + line)
      }
    }
    source.close()

    //    frequency.keys.toSeq.sorted.foreach { key =>
    //      println(key + "   " + frequency(key))
    //    }

    frequency.toSeq.sortBy(_._2).reverse.take(100).foreach { case (key, value) =>
      println(key + "   " + value)
    }
  }

  def analyze(filename: String, logfile: String): Unit = {
    val parser: Parser[LogRecord] = new HttpdLoglineParser[LogRecord](classOf[LogRecord], LOG_FORMAT)
    val source = Source.fromFile(filename)
    val records = source.getLines().flatMap { line =>
      try {
        Some(parser.parse(line))
      }
      catch {
        case e: DissectionFailure =>
          println("ERROR: " + line)
          None
      }
    }

    process(logfile, records)
    source.close()
  }

  @tailrec
  private def process(logfile: String, records: Iterator[LogRecord], contextOption: Option[LogAnalysisContext] = None): Unit = {
    if (records.hasNext) {
      val record = records.next()
      val nextContext = contextOption match {
        case None =>
          val context = LogAnalysisContext(logfile, record.key)
          analyze(record, context)

        case Some(context) =>
          if (context.key == record.key) {
            val nextContext = analyze(record, context.newRecord())
            nextContext
          }
          else {
            save(context)
            // printNonRobotValues(context)
            val newContext = LogAnalysisContext(logfile, record.key)
            analyze(record, newContext)
          }
      }
      process(logfile, records, Some(nextContext))
    }
  }

  private def analyze(record: LogRecord, context: LogAnalysisContext): LogAnalysisContext = {
    val analyzers = List(
      RobotAnalyzer, // this has to be done first!
      AnalysisAnalyzer,
      ApiAnalyzer,
      ApplicationAnalyzer,
      AssetAnalyzer,
      TileAnalyzer
    )
    doAnalyze(analyzers, record, context)
  }

  @tailrec
  private def doAnalyze(analyzers: List[LogRecordAnalyzer], record: LogRecord, context: LogAnalysisContext): LogAnalysisContext = {
    if (analyzers.isEmpty) {
      context
    }
    else {
      val newContext = analyzers.head.analyze(record, context)
      doAnalyze(analyzers.tail, record, newContext)
    }
  }

  private def printNonRobotValues(context: LogAnalysisContext): Unit = {
    val nonRobotValues = context.values.filter { case (key, value) => !key.endsWith("robot") }
    if (nonRobotValues.nonEmpty) {
      println(context.key + " " + nonRobotValues)
    }
  }

  private def save(context: LogAnalysisContext): Unit = {
    val actionTimestamp = ActionTimestamp.from(Timestamp.fromLogKey(context.key))
    val values = context.values.toSeq.map { case (key, value) => LogValue(key, value) }
    val logAction = LogAction(actionTimestamp, context.logfile, values)
    frontendMetricsRepository.saveLogAction(logAction)
  }
}
