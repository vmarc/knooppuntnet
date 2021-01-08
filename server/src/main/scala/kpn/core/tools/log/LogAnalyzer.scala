package kpn.core.tools.log

import kpn.core.tools.log.analyzers.AnalysisAnalyzer
import kpn.core.tools.log.analyzers.ApiAnalyzer
import kpn.core.tools.log.analyzers.ApplicationAnalyzer
import kpn.core.tools.log.analyzers.AssetAnalyzer
import kpn.core.tools.log.analyzers.LogRecordAnalyzer
import kpn.core.tools.log.analyzers.RobotAnalyzer
import kpn.core.tools.log.analyzers.TileAnalyzer
import nl.basjes.parse.core.Parser
import nl.basjes.parse.httpdlog.HttpdLoglineParser

import scala.annotation.tailrec
import scala.io.Source
import scala.jdk.CollectionConverters._

object LogAnalyzer {
  def main(args: Array[String]): Unit = {
    new LogAnalyzer().printPaths()
  }
}

class LogAnalyzer {

  private val LOG_FORMAT = "$remote_addr - $remote_user [$time_local] \"$host\" \"$request\" $status $body_bytes_sent \"$http_referer\" \"$http_user_agent\"rt=$request_time uct=\"upstream_connect_time\" uht=\"upstream_header_time\" urt=\"$upstream_response_time\""

  def printPossiblePaths(): Unit = {
    val dummyParser: Parser[Object] = new HttpdLoglineParser[Object](classOf[Object], LOG_FORMAT)
    val possiblePaths = dummyParser.getPossiblePaths.asScala
    for (path <- possiblePaths) {
      println(path)
    }
  }

  def printPaths(): Unit = {
    val parser: Parser[LogRecord] = new HttpdLoglineParser[LogRecord](classOf[LogRecord], LOG_FORMAT)
    val source = Source.fromFile("/kpn/logs/tmp.log")
    val records = source.getLines.map { line =>
      val record = parser.parse(line)
      val analysis = analyze(record)
      if (analysis.other) {
        println(record.path)
      }
      analysis
    }.toSeq
    source.close()

    println("")
    println(s"records=${records.size}/${records.count(_.robot)}")
    println(s"api=${records.count(_.api)}/${records.count(r => r.robot && r.api)}")
    println(s"tiles=${records.count(_.tile)}/${records.count(r => r.robot && r.tile)}")
    println(s"analysis=${records.count(_.analysis)}/${records.count(r => r.robot && r.analysis)}")
    println(s"assets=${records.count(_.asset)}/${records.count(r => r.robot && r.asset)}")
    println(s"application=${records.count(_.application)}/${records.count(r => r.robot && r.application)}")
    println(s"other=${records.count(_.other)}/${records.count(r => r.robot && r.other)}")
  }

  private def analyze(record: LogRecord): LogRecordAnalysis = {
    val analyzers = List(
      RobotAnalyzer,
      AnalysisAnalyzer,
      ApiAnalyzer,
      ApplicationAnalyzer,
      AssetAnalyzer,
      TileAnalyzer
    )
    doAnalyze(analyzers, record, LogRecordAnalysis())
  }

  @tailrec
  private def doAnalyze(analyzers: List[LogRecordAnalyzer], record: LogRecord, analysis: LogRecordAnalysis): LogRecordAnalysis = {
    if (analyzers.isEmpty) {
      analysis
    }
    else {
      val newAnalysis = analyzers.head.analyze(record, analysis)
      doAnalyze(analyzers.tail, record, newAnalysis)
    }
  }

}
