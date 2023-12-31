package kpn.core.tools.support

import kpn.api.common.ReplicationId
import kpn.api.custom.ApiResponse
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.TimestampUtil
import kpn.server.json.Json
import org.apache.commons.io.FileUtils
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

import java.io.File
import java.nio.charset.Charset
import scala.xml.XML

object FindDeletedNodesHistoryTool {
  def main(args: Array[String]): Unit = {
    // writeFiles()
    analyzeFiles()
  }

  private def writeFiles(): Unit = {
    deletedNodeIds().foreach { nodeId =>
      println(nodeId)
      history(nodeId)
      Thread.sleep(5000)
    }
  }

  private def analyzeFiles(): Unit = {

    case class HistoryEntry(
      visible: Boolean,
      timestamp: Timestamp,
      changeSetId: Long,
      tags: Tags
    )

    case class HistoryAnalysis(
      nodeId: Long,
      createdTimestamp: Timestamp,
      deletedTimestamp: Timestamp,
      deletedChangeSetId: Long,
      deletedReplicationId: Option[ReplicationId],
      tags: Boolean
    )

    val analysisCollection = deletedNodeIds().map { nodeId =>
      val xmlString = FileUtils.readFileToString(new File(s"/kpn/deleted/$nodeId.xml"), "UTF-8")
      val xml = XML.loadString(xmlString)
      val historyEntries = (xml \ "node").map { n =>
        val visible = false // (n \ "@visible").text.toBoolean
        val timestamp = TimestampUtil.parseIso((n \ "@timestamp").text)
        val changeSetId = (n \ "@changeset").text.toLong
        val tags = Tags(
          (n \ "tag").map { t =>
            val key = (t \ "@k").text
            val value = (t \ "@v").text
            Tag(key, value)
          }
        )
        HistoryEntry(
          visible,
          timestamp,
          changeSetId,
          tags
        )
      }

      val createdTimestamp = historyEntries.head.timestamp
      val deletedTimestamp = historyEntries.last.timestamp
      val deletedChangeSetId = historyEntries.last.changeSetId
      val tagsBeforeDelete = historyEntries.dropRight(1).last.tags
      val tags = tagsBeforeDelete.has("rwn_ref") ||
        tagsBeforeDelete.has("rcn_ref") ||
        tagsBeforeDelete.has("rpn_ref")

      val replicationId = changeSetReplicationNumber(deletedChangeSetId)

      HistoryAnalysis(
        nodeId,
        createdTimestamp,
        deletedTimestamp,
        deletedChangeSetId,
        replicationId,
        tags
      )
    }

    println("<table>")
    println("<tr>")
    println("<th rowspan='2'>Nr</th>")
    println("<th colspan='3'>Node</th>")
    println("<th colspan='4'>Deleted</th>")
    println("<th rowspan='2'>Created</th>")
    println("</tr>")

    println("<tr>")
    println("<th>knooppuntnet</th>")
    println("<th colspan='2'>osm</th>")
    println("<th>Date</th>")
    println("<th>Changeset</th>")
    println("<th>Replication</th>")
    println("<th>Tags*</th>")
    println("</tr>")

    analysisCollection.sortBy(_.deletedTimestamp).reverse.zipWithIndex.foreach { case (analysis, index) =>
      println("<tr>")

      println("<td>")
      println(s"${index + 1}")
      println("</td>")

      println("<td>")
      println(s"<a href='https://knooppuntnet.nl/en/analysis/node/${analysis.nodeId})'>${analysis.nodeId}</a>")
      println("</td>")

      println("<td>")
      println(s"<a href='https://www.openstreetmap.org/node/${analysis.nodeId}'>node</a>")
      println("</td>")

      println("<td>")
      println(s"<a href='https://www.openstreetmap.org/node/${analysis.nodeId}/history'>history</a>")
      println("</td>")

      println("<td>")
      println(analysis.deletedTimestamp.yyyymmdd)
      println("</td>")

      println("<td>")
      println(analysis.deletedReplicationId.map(replicationId => s"<a href='https://knooppuntnet.nl/en/analysis/changeset/${analysis.deletedChangeSetId}/${replicationId.number}'>${analysis.deletedChangeSetId}</a>").getOrElse(s"${analysis.deletedChangeSetId}"))
      println("</td>")

      println("<td>")
      println(analysis.deletedReplicationId.map(_.name).getOrElse("?"))
      println("</td>")

      println("<td>")
      println(if (analysis.tags) "Yes" else "No")
      println("</td>")

      println("<td>")
      println(analysis.createdTimestamp.yyyymmdd)
      println("</td>")

      println("</tr>")
    }
    println("</table>")
  }

  private def changeSetReplicationNumber(changeSetId: Long): Option[ReplicationId] = {
    val restTemplate = new RestTemplate
    val url = s"http://localhost:9005/api/replication/$changeSetId"
    val headers = new HttpHeaders()
    headers.setAccept(java.util.Arrays.asList(MediaType.APPLICATION_JSON))
    headers.setAcceptCharset(java.util.Arrays.asList(Charset.forName("UTF-8")))
    val entity = new HttpEntity[String]("", headers)
    val response = restTemplate.exchange(url, HttpMethod.GET, entity, classOf[String])
    if (response.getStatusCode == HttpStatus.OK) {
      val apiResponse = Json.objectMapper.readValue(response.getBody, classOf[ApiResponse[Long]])
      apiResponse.result.map(replicationNumber => ReplicationId(replicationNumber))
    }
    else {
      None
    }
  }

  private def history(nodeId: Long): Unit = {
    val restTemplate = new RestTemplate
    val url = s"https://api.openstreetmap.org/api/0.6/node/$nodeId/history"
    val headers = new HttpHeaders()
    headers.setAccept(java.util.Arrays.asList(MediaType.TEXT_XML))
    headers.setAcceptCharset(java.util.Arrays.asList(Charset.forName("UTF-8")))
    headers.set(HttpHeaders.REFERER, "knooppuntnet.nl")
    val entity = new HttpEntity[String]("", headers)

    val response: ResponseEntity[String] = restTemplate.exchange(url, HttpMethod.GET, entity, classOf[String])
    if (response.getStatusCode == HttpStatus.OK) {
      val xmlString = response.getBody
      FileUtils.writeStringToFile(new File(s"/kpn/deleted/$nodeId.xml"), xmlString, "UTF-8")
    }
  }

  private def deletedNodeIds(): Seq[Long] = {
    Seq(
      1015553600L,
      1069126190L,
      1079550304L,
      1091801299L,
      1116648353L,
      1229663673L,
      1300803984L,
      1437507756L,
      1550619438L,
      1562417659L,
      1589998354L,
      1907524431L,
      1958482331L,
      2083085730L,
      2390428852L,
      2467944970L,
      2467944977L,
      247743093L,
      282519456L,
      288913899L,
      295612796L,
      299554924L,
      3040050638L,
      304810916L,
      3152816171L,
      317296390L,
      3251067775L,
      379736577L,
      393446185L,
      417027370L,
      4182906318L,
      42263958L,
      43000695L,
      43166010L,
      43398756L,
      44849641L,
      47831214L,
      47904321L,
      48153404L,
      48178247L,
      487466419L,
      492094570L,
      492493149L,
      495469737L,
      501919469L,
      506079097L,
      5087863050L,
      5554969611L,
      664040428L,
      683086398L,
      727291652L,
      7340651923L,
      7340651924L,
      739277514L,
      793717909L,
      8078748268L,
      8163931867L,
      8460255625L,
      8731919671L,
      972574220L,
      997744665L,
    )
  }
}
