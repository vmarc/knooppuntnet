package kpn.core.replicate

import akka.stream.ActorMaterializer
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.coding.Gzip
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.MediaTypes
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers
import akka.util.ByteString
import kpn.shared.ReplicationId

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.Duration

class ReplicationRequestExecutorImpl(system: ActorSystem) extends ReplicationRequestExecutor {

  private val URL = "https://planet.osm.org/replication/minute/"

  private implicit val implicitSystem = system
  private implicit val dispatcher = system.dispatcher
  private implicit val materializer = ActorMaterializer()

  def requestChangesFile(replicationId: ReplicationId): Option[String] = {

    val uri = URL + replicationId.name + ".osc.gz"

    val request = HttpRequest(
      uri = uri,
      headers = List(headers.Accept(MediaTypes.`application/x-gzip`))
    )

    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)
    val response: HttpResponse = Await.result(responseFuture, Duration.Inf)

    response match {
      case HttpResponse(StatusCodes.OK, headers, entity, _) =>
        val stringFuture = entity.dataBytes.runFold(ByteString.empty)(_ ++ _).map { body =>
          val bodyFuture = Await.result(Gzip.decode(body), Duration.Inf)
          bodyFuture.utf8String
        }
        val string = Await.result(stringFuture, Duration.Inf)
        Some(string)

      case resp@HttpResponse(StatusCodes.NotFound, _, _, _) =>
        resp.discardEntityBytes()
        None

      case resp@HttpResponse(code, _, _, _) =>
        println("Request failed, response code: " + code)
        resp.discardEntityBytes()
        None
    }
  }

  def requestStateFile(replicationId: ReplicationId): Option[String] = {
    val uri = URL + replicationId.name + ".state.txt"
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = uri))
    val response: HttpResponse = Await.result(responseFuture, Duration.Inf)
    response match {
      case HttpResponse(StatusCodes.OK, headers, entity, _) =>
        val bodyFuture = entity.dataBytes.runFold(ByteString.empty)(_ ++ _).map(_.utf8String)
        val body = Await.result(bodyFuture, Duration.Inf)
        Some(body)

      case resp@HttpResponse(StatusCodes.NotFound, _, _, _) =>
        resp.discardEntityBytes()
        None

      case resp@HttpResponse(code, _, _, _) =>
        println("Request failed, response code: " + code)
        resp.discardEntityBytes()
        None
    }
  }
}
