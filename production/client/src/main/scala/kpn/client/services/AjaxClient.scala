// Migrated to Angular
package kpn.client.services

import java.nio.ByteBuffer

import boopickle.DefaultBasic._
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.typedarray._
import scala.util.Failure
import scala.util.Success

object AjaxClient extends autowire.Client[ByteBuffer, Pickler, Pickler] {
  override def doCall(req: Request): Future[ByteBuffer] = {
    dom.ext.Ajax.post(
      url = "/api/" + req.path.mkString("/"),
      data = Pickle.intoBytes(req.args),
      responseType = "arraybuffer",
      headers = Map("Content-Type" -> "application/octet-stream"),
      timeout = 25000
    ).map(r => TypedArrayBuffer.wrap(r.response.asInstanceOf[ArrayBuffer]))
  }

  override def read[Result: Pickler](p: ByteBuffer): Result = Unpickle[Result].fromBytes(p)

  override def write[Result: Pickler](r: Result): ByteBuffer = Pickle.intoBytes(r)

  def getResponse(doc: String, callback: (dom.XMLHttpRequest) => Unit): Unit = {
    val future: Future[dom.XMLHttpRequest] = get(doc)
    //    val request: dom.XMLHttpRequest = Await.result(future, Duration.Inf)
    //    println("request=" +request)

    future.onComplete {
      case Success(request) => callback(request)
      case Failure(t) => println("An error has occured: " + t.getMessage)
    }
  }

  def get(doc: String): Future[dom.XMLHttpRequest] = {
    dom.ext.Ajax.get(
      url = "/docs/" + doc,
      //      data = Pickle.intoBytes(req.args),
      //      responseType = "arraybuffer",
      //      headers = Map("Content-Type" -> "application/octet-stream"),
      timeout = 25000
    )
  }
}
