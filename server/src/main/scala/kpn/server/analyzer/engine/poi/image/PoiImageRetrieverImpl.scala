package kpn.server.analyzer.engine.poi.image

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.JpegWriter
import kpn.api.common.PoiState
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.analyzer.engine.poi.PoiRef
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

import java.nio.charset.Charset

class PoiImageRetrieverImpl(
  poiImageRepository: PoiImageRepositoryImpl
) {

  private val restTemplate = new RestTemplate(converters)
  private val log = Log(classOf[PoiImageRetrieverImpl])

  def retrieveImage(poiRef: PoiRef, url: String): PoiState = {
    Log.context(s"${poiRef.toId}") {
      log.info(url)
      val headers = new HttpHeaders()
      // headers.set("Accept", MediaType.IMAGE_JPEG_VALUE)
      val entity = new HttpEntity[Array[Byte]](Array[Byte](), headers)
      try {
        val response: ResponseEntity[Array[Byte]] = restTemplate.exchange(url, HttpMethod.GET, entity, classOf[Array[Byte]])
        if (response.getStatusCode == HttpStatus.MOVED_PERMANENTLY) {
          val newUrl = response.getHeaders.getLocation.toString
          log.info("moved permanently to: " + newUrl)
          val result = retrieveImage(poiRef, newUrl)
          PoiState(
            _id = poiRef.toId,
            imageLink = result.imageLink,
            imageStatus = Some("MovedPermanently-301|" + result.imageStatus.mkString),
            imageStatusDetail = Some(newUrl),
            imageLastSeen = result.imageLastSeen
          )
        }
        if (response.getStatusCode == HttpStatus.FOUND) {
          // TODO report in PoiState, example http redirected to https
          val newUrl = response.getHeaders.getLocation.toString
          log.info("moved permanently to: " + newUrl)
          retrieveImage(poiRef, newUrl)
          val result = retrieveImage(poiRef, newUrl)
          PoiState(
            _id = poiRef.toId,
            imageLink = result.imageLink,
            imageStatus = Some("MovedPermanently-302|" + result.imageStatus.mkString),
            imageStatusDetail = Some(newUrl),
            imageLastSeen = result.imageLastSeen
          )
        }
        else if (response.getStatusCode != HttpStatus.OK) {
          log.warn("not success: " + response.getStatusCode)
          PoiState(
            _id = poiRef.toId,
            imageLink = Some(url),
            imageStatus = Some(s"not-ok-${response.getStatusCode}")
          )
        }
        else {
          val bytes = response.getBody
          val image = ImmutableImage.loader().fromBytes(bytes)

          val thumbnail = image.max(100, 100)
          val file = poiImageRepository.file(poiRef)
          log.info(s"${file.getAbsolutePath}, image=${image.dimensions.getX}x${image.dimensions.getY}, thumbnail=${thumbnail.dimensions.getX}x${thumbnail.dimensions.getY}")
          thumbnail.output(JpegWriter.Default, file)
          PoiState(
            _id = poiRef.toId,
            imageLink = Some(url),
            imageStatus = Some("ok"),
            imageLastSeen = Some(Time.now)
          )
        }
      }
      catch {
        case e: HttpClientErrorException.BadRequest =>
          log.error(s"Could not execute query $url\n${e.getStatusText}", e)
          PoiState(
            _id = poiRef.toId,
            imageLink = Some(url),
            imageStatus = Some("BadRequest"),
            imageStatusDetail = Some(e.getStatusText)
          )
        case e: HttpClientErrorException.NotFound =>
          if (url.contains("upload.wikimedia.org/wikipedia/commons") && url.contains(" ")) {
            val newUrl = url.replaceAll(" ", "_")
            log.info("try again with " + newUrl)
            retrieveImage(poiRef, newUrl)
          }
          else {
            log.warn("not found")
            PoiState(
              _id = poiRef.toId,
              imageLink = Some(url),
              imageStatus = Some("NotFound")
            )
          }
        case e =>
          val detail = e.getClass.getName + " " + e.getMessage
          log.warn(detail)
          PoiState(
            _id = poiRef.toId,
            imageLink = Some(url),
            imageStatus = Some("Exception"),
            imageStatusDetail = Some(detail),
          )
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
