package kpn.tools.har

import de.sstoehr.harreader.HarReader
import de.sstoehr.harreader.model.Har
import de.sstoehr.harreader.model.HarContent
import de.sstoehr.harreader.model.HarEntry
import de.sstoehr.harreader.model.HarHeader
import de.sstoehr.harreader.model.HarPostData
import de.sstoehr.harreader.model.HarRequest
import de.sstoehr.harreader.model.HarResponse
import de.sstoehr.harreader.model.HttpStatus
import kpn.server.json.Json

import java.io.File
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import scala.jdk.CollectionConverters._

object HttpArchiveReportTool {
  def main(args: Array[String]): Unit = {
    new HttpArchiveReportTool().report("/home/vmarc/test.har")
  }
}

class HttpArchiveReportTool {

  def report(filename: String): Unit = {
    val har = readHar(filename)
    har.getLog.getEntries.asScala.filter(includeEntry).foreach(reportEntry)
  }

  private def reportEntry(entry: HarEntry): Unit = {
    reportRequest(entry.getRequest)
    reportResponse(entry.getResponse)
  }

  private def reportRequest(request: HarRequest): Unit = {
    println("---")
    val url = URLDecoder.decode(request.getUrl, StandardCharsets.UTF_8)
    println(s"${request.getMethod} ${url}")
    printHeaders(request.getHeaders.asScala.toSeq)
    printPostData(request.getPostData)
  }

  private def reportResponse(response: HarResponse): Unit = {
    println()

    val status = HttpStatus.byCode(response.getStatus)
    println(s"Response ${status.getCode} ${status.name()}")
    printHeaders(response.getHeaders.asScala.toSeq)
    printResponseContent(response.getContent)
  }

  private def printHeaders(headers: Seq[HarHeader]): Unit = {
    println("Headers")
    headers.foreach { header =>
      println("  " + header.getName + ": " + header.getValue)
    }
  }

  private def printPostData(postData: HarPostData): Unit = {
    if (postData != null && postData.getParams.asScala.toSeq.nonEmpty) {
      println("postData")
      postData.getParams.asScala.toSeq.foreach { param =>
        println(s"  ${param.getName}: ${param.getValue}")
      }
    }
  }

  def printResponseContent(content: HarContent): Unit = {
    if (content != null) {
      println("Content: mimeType=" + content.getMimeType)
      if (content.getMimeType != null && content.getMimeType.contains("text/html")) {
        // println("  html content not printed")
        println(content.getText)
      }
      else if (content.getMimeType != null && content.getMimeType.contains("application/json")) try {
        val jsonObject = Json.value[Object](content.getText, classOf[Object])
        val json = Json.objectMapper.writerWithDefaultPrettyPrinter()
        println(json.writeValueAsString(jsonObject))
      }
      catch {
        case e: Exception => println(content.getText)
      }
      else {
        if (content.getText != null) {
          println(content.getText)
        }
      }
    }
  }

  private def readHar(filename: String): Har = {
    val harReader = new HarReader()
    val file = new File(filename)
    harReader.readFromFile(file)
  }

  private def includeEntry(entry: HarEntry): Boolean = {
    val url = entry.getRequest.getUrl
    val excludedExtensions = Seq(
      ".js",
      ".png",
      ".svg",
      ".ico",
      ".css",
      ".woff2",
      "manifest.json",
      "poi-configuration"
    )

    val excludedNames = Seq(
      "matomo.openstreetmap.org",
      "assets/manifest",
      "@vite/client",
      "googleapis",
      "/@fs/",
      "ws://"
    )

    !(excludedExtensions.exists(url.endsWith) || excludedNames.exists(url.contains))
  }
}
