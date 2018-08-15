package controllers

import java.nio.ByteBuffer

import boopickle.DefaultBasic._
import kpn.shared.Api
import play.api.mvc.Result
import play.api.mvc.Results
import services.ApiService

import scala.concurrent.Future

// This import is needed, despite what IntelliJ thinks
import kpn.shared.KpnPicklers._

class AutoWireService {

  import scala.concurrent.ExecutionContext.Implicits.global

  def unpickle(path: String, byteBuffer: ByteBuffer, apiService: ApiService): Future[Result] = {
    val args = Unpickle[Map[String, ByteBuffer]].fromBytes(byteBuffer)
    Router.route[Api](apiService)(
      autowire.Core.Request(path.split("/"), args)
    ).map(buffer => {
      val data = Array.ofDim[Byte](buffer.remaining())
      buffer.get(data)
      Results.Ok(data)
    })
  }
}
