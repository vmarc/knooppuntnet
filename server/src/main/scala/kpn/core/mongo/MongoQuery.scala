package kpn.core.mongo

import org.mongodb.scala.bson.BsonDocument

import java.io.InputStream
import scala.io.Source

class MongoQuery {

  def readPipeline(pipelineName: String): Pipeline = {
    val lines = readPipelineFile(pipelineName)
    val strings = toStageStrings(lines)
    val stages = strings.map { string =>
      BsonDocument(string)
    }
    Pipeline(pipelineName, stages)
  }

  private def readPipelineFile(pipelineName: String): List[String] = {
    val classFilename = "/" + getClass.getCanonicalName.replaceAll("\\.", "/").replaceAll("\\$", "")
    val pipelineFilename = s"$classFilename-$pipelineName.json"
    try {
      val stream: InputStream = getClass.getResourceAsStream(pipelineFilename)
      Source.fromInputStream(stream).getLines.toList
    }
    catch {
      case e: Exception => throw new IllegalStateException(s"Could not read $pipelineFilename", e)
    }
  }

  private def toStageStrings(lines: List[String]): List[String] = {
    val stagesLines = lines.filterNot(_ == "[").filterNot(_ == "]")
    val stageLineLists = splitupPerStage(stagesLines)
    stageLineLists.map(stageLines => stageLines.mkString)
  }

  private def splitupPerStage(lines: List[String]): List[List[String]] = {
    val separator = "  },"
    lines.span(_ != separator) match {
      case (hd, _ :: tl) => (hd :+ separator) :: splitupPerStage(tl)
      case (hd, _) => List(hd)
    }
  }
}
