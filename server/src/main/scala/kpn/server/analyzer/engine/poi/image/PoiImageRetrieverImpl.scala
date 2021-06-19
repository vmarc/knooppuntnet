package kpn.server.analyzer.engine.poi.image

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.JpegWriter
import kpn.api.common.PoiAnalysis
import kpn.core.db.couch.Couch
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.analyzers.PoiImageAnalyzer
import kpn.server.repository.PoiRepositoryImpl
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.nio.charset.Charset
import scala.io.Source

object PoiImageRetrieverImpl {

  def main2(args: Array[String]): Unit = {

    Couch.executeIn("kpn-server", "pois4") { database =>
      val repo = new PoiRepositoryImpl(null, database, false)

      val poiRefs = {
        val source = Source.fromFile("/kpn/tmp/pois.txt")
        try {
          source.getLines().toList.map { line =>
            val splitted = line.split(":")
            PoiRef(splitted(0), splitted(1).toLong)
          }
        }
        finally {
          source.close()
        }
      }

      val out = new PrintWriter(new FileWriter("/kpn/tmp/poi-links.txt"))

      println(s"Processing ${poiRefs.size} pois")
      poiRefs.zipWithIndex.foreach { case (poiRef, index) =>
        if (((index + 1) % 50) == 0) {
          println(s"${index + 1}/${poiRefs.length}")
        }

        repo.get(poiRef) match {
          case Some(poi) =>

            val context = new PoiImageAnalyzer(
              PoiAnalysisContext(
                poi,
                Seq.empty,
                Seq.empty,
                Seq.empty,
                PoiAnalysis()
              )
            ).analyze

            context.analysis.image match {
              case Some(url) =>
                println(s"image|${poiRef.elementType}|${poiRef.elementId}|$url")
                out.println(s"image|${poiRef.elementType}|${poiRef.elementId}|$url")
              case None =>
            }

            context.analysis.mapillary match {
              case Some(url) =>
                println(s"mapillary|${poiRef.elementId}|$url")
                out.println(s"mapillary|${poiRef.elementId}|$url")
              case None =>
            }

          case None =>
        }
      }
      out.close()
    }
  }

  def main(args: Array[String]): Unit = {

    case class PoiLink(linkType: String, elementType: String, elementId: Long, url: String) {
      def toRef: PoiRef = PoiRef(elementType, elementId)
    }

    val imageRepo = new PoiImageRepositoryImpl()

    val retriever = new PoiImageRetrieverImpl()
    val poiLinks = {
      val source = Source.fromFile("/kpn/tmp/poi-links.txt")
      try {
        source.getLines().toList.map { line =>
          val splitted = line.split("\\|")
          PoiLink(splitted(0), splitted(1), splitted(2).toLong, splitted(3))
        }
      }
      finally {
        source.close()
      }
    }

    println(s"Processing ${poiLinks.size} links")
    poiLinks.zipWithIndex.foreach { case (poiLink, index) =>
      if (!imageRepo.exists(poiLink.toRef)) {
        println(s"${index + 1}/${poiLinks.size} id=${poiLink.elementType}:${poiLink.elementId}, url=${poiLink.url}")
        try {
          retriever.retrieveImage(poiLink.toRef, poiLink.url)
        }
        catch {
          case e: Exception =>
            println("Could not process image" + e.getMessage)
        }
      }
    }
  }

}

class PoiImageRetrieverImpl {

  private val restTemplate = new RestTemplate(converters)

  def retrieveImage(poiRef: PoiRef, url: String): Unit = {

    val headers = new HttpHeaders()
    // headers.set("Accept", MediaType.IMAGE_JPEG_VALUE)
    val entity = new HttpEntity[Array[Byte]](Array[Byte](), headers)
    try {
      val response: ResponseEntity[Array[Byte]] = restTemplate.exchange(url, HttpMethod.GET, entity, classOf[Array[Byte]])

      if (response.getStatusCode == HttpStatus.MOVED_PERMANENTLY) {
        val newUrl = response.getHeaders.getLocation.toString
        retrieveImage(poiRef, newUrl)
      }
      else if (response.getStatusCode != HttpStatus.OK) {
        println("not success")
      }
      else {
        val bytes = response.getBody
        val image = ImmutableImage.loader().fromBytes(bytes)

        val thumbnail = image.max(100, 100)
        print(s"poi=${poiRef.elementType}/${poiRef.elementId}, image=${image.dimensions.getX}x${image.dimensions.getY}, ")
        println(s"thumbnail=${thumbnail.dimensions.getX}x${thumbnail.dimensions.getY}")

        val s = poiRef.elementId.toString
        val dir = s"/kpn/images/${s(s.length - 2)}/${s(s.length - 1)}"
        new File(dir).mkdirs()
        val file = new File(dir, s"${poiRef.elementType}-${poiRef.elementId}.jpg")

        thumbnail.output(JpegWriter.Default, file)
      }
    }
    catch {
      case e: HttpClientErrorException.BadRequest =>
        throw new IllegalStateException(s"Could not execute query $url\n${e.getStatusText}", e)
      case e: HttpClientErrorException.NotFound =>
        if (url.contains("upload.wikimedia.org/wikipedia/commons") && url.contains(" ")) {
          val newUrl = url.replaceAll(" ", "_")
          println("try again with " + newUrl)
          retrieveImage(poiRef, newUrl)
        }
        else {
          println("not found")
        }
    }
  }

  private def converters: java.util.List[HttpMessageConverter[_]] = {
    java.util.Arrays.asList(
      new ByteArrayHttpMessageConverter(),
      new StringHttpMessageConverter(Charset.forName("UTF-8"))
    )
  }
}
