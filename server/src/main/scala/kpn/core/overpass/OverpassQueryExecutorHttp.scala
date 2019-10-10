package kpn.core.overpass

import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import kpn.core.util.Log
import spray.can.Http
import spray.http.HttpEncodingRange.apply
import spray.http.HttpEncodings.gzip
import spray.http.HttpHeaders._
import spray.http.HttpMethods.GET
import spray.http.HttpRequest
import spray.http.HttpResponse
import spray.http.StatusCodes
import spray.http.Uri
import spray.httpx.encoding.Gzip

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration

class OverpassQueryExecutorHttp(system: ActorSystem, address: String) extends OverpassQueryExecutor {

  private val delay: FiniteDuration = 5.seconds

  private val overpassTimeout: Timeout = Timeout(900.seconds)

  private val log = Log(classOf[OverpassQueryExecutorHttp])

  def execute(queryString: String): String = {
    import scala.concurrent.ExecutionContext.Implicits.global
    system.scheduler.scheduleOnce(delay) {

    }

    log.debug(queryString)
    val t1 = System.currentTimeMillis()

    val responseFuture: Future[HttpResponse] = (IO(Http)(system).ask(request(queryString))(overpassTimeout)).mapTo[HttpResponse]
    val response = Await.result(responseFuture, Duration.Inf)
    response.status match {
      case StatusCodes.OK =>

        val result = new String(Gzip.newDecompressor.decompress(response.entity.data.toByteArray))
        val t2 = System.currentTimeMillis()
        log.debug("Reponse size %d in %dms".format(response.entity.data.length, t2 - t1))
        result

      case _ =>
        throw new RuntimeException("Request=" + queryString +
          "\nCould not process reponse: " + response
        )
    }
  }

  private def request(query: String): HttpRequest = {
    val uri = Uri(address + "interpreter").
      withQuery(
        "data" -> query
      )
    val headers = List(`Accept-Encoding`(gzip))
    HttpRequest(GET, uri, headers)
  }
}
