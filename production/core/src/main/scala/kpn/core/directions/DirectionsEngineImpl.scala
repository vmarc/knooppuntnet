package kpn.core.directions

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ContentType
import akka.http.scaladsl.model.HttpCharsets
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.MediaType
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Query
import akka.stream.ActorMaterializer
import akka.util.ByteString
import akka.util.Timeout
import kpn.core.db.couch.Couch
import kpn.core.db.json.JsonFormatsDirections
import kpn.core.facade.pages.directions.GraphHopperDirections
import spray.json.JsonParser

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

class DirectionsEngineImpl(
  system: ActorSystem,
  materializer: ActorMaterializer,
  graphhopperApiKey: String
) extends DirectionsEngine {

  implicit val actorSystem: ActorSystem = system
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val actorMaterializer: ActorMaterializer = materializer

  def get(language: String, gpx: String): Option[GraphHopperDirections] = {

    val timeout: Timeout = Couch.batchTimeout
    val uri = Uri("https://graphhopper.com/api/1/match")

    val query = Query(
      "locale" -> language,
      "gps_accuracy" -> "20",
      "max_visited_nodes" -> "3000",
      "type" -> "json",
      "instructions" -> "true",
      "points_encoded" -> "false",
      "elevation" -> "false",
      "vehicle" -> "foot",
      "key" -> graphhopperApiKey
    )

    val mediaType = MediaType.customWithFixedCharset("application", "gpx+xml", HttpCharsets.`UTF-8`)
    val requestEntity = HttpEntity(ContentType(mediaType), gpx)
    val request = HttpRequest(
      HttpMethods.POST,
      uri.withQuery(query),
      entity = requestEntity
    )

    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)
    val response: HttpResponse = Await.result(responseFuture, Duration.Inf)

    println(response)

    val rateLimitLimit = response.headers.find(h => h.name() == "X-RateLimit-Limit")
    val rateLimitRemaining = response.headers.find(h => h.name() == "X-RateLimit-Remaining")
    val rateLimitReset = response.headers.find(h => h.name() == "X-RateLimit-Reset")
    val rateLimitCredits = response.headers.find(h => h.name() == "X-RateLimit-Credits")

    println("rateLimitLimit=" + rateLimitLimit)
    println("rateLimitRemaining=" + rateLimitRemaining)
    println("rateLimitReset=" + rateLimitReset)
    println("rateLimitCredits=" + rateLimitCredits)

    response match {
      case HttpResponse(StatusCodes.OK, headers, entity, _) =>
        val stringFuture = entity.dataBytes.runFold(ByteString.empty)(_ ++ _).map { body =>
          body.utf8String
        }
        val string = Await.result(stringFuture, Duration.Inf)

        println(string)

        val json = JsonParser(string)
        val directions = JsonFormatsDirections.graphHopperDirectionsFormat.read(json)
        Some(directions)

      case resp@HttpResponse(StatusCodes.NotFound, _, _, _) =>
        resp.discardEntityBytes()
        None

      case resp@HttpResponse(code, _, _, _) =>
        println("Request failed, response code: " + code)

        println(response)

        resp.discardEntityBytes()
        None
    }

  }
}
