package kpn.core.db.couch

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.MediaTypes
import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.util.Timeout
import kpn.core.database.views.common.{Design, View, ViewRow}
import spray.json.JsArray
import spray.json.JsFalse
import spray.json.JsNumber
import spray.json.JsObject
import spray.json.JsString
import spray.json.JsValue
import spray.json.deserializationError
import spray.json.pimpString

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class OldDatabaseImpl(couch: Couch, val name: String) extends OldDatabase {

  implicit val system: ActorSystem = couch.sys
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = couch.materializer

  override def toString: String = {
    s"${getClass.getSimpleName}($name)"
  }

  def optionGets(query: String, timeout: Timeout): Option[String] = {
    val uri = docUrl(query)
    val request = HttpRequest(HttpMethods.GET, uri)
    performRequest2(request) { (response, entityString) =>
      response.status match {
        case StatusCodes.NotFound => None
        case StatusCodes.OK => Some(entityString)
        case _ => throw new RuntimeException(s"""GET Request failed: "$uri": ${response.status} - $entityString""")
      }
    }
  }

  private def getJsonString(request: String, timeout: Timeout): String = {
    val httpRequest = HttpRequest(uri = docUrl(request))
    performRequest2(httpRequest, timeout) { (response: HttpResponse, entityString: String) =>
      response.status match {
        case StatusCodes.Success(_) => entityString
        case status: StatusCode => throw new RuntimeException(s"""Request failed: "${httpRequest.uri}": $status - ${response.entity}""")
      }
    }
  }

  private def getJsValue(request: String, timeout: Timeout): JsValue = {
    getJsonString(request, timeout).parseJson
  }

  override def getRows(request: String, timeout: Timeout): Seq[JsValue] = {
    getJsValue(request, timeout) match {
      case obj: JsObject =>
        obj.getFields("rows") match {
          case Seq(array: JsArray) => array.elements
          case _ => Seq()
        }
      case _ => deserializationError("'rows' field expected")
    }
  }

  override def objectsWithIds(ids: Seq[String], timeout: Timeout, stale: Boolean): Seq[JsValue] = {
    if (ids.isEmpty) {
      Seq()
    }
    else {
      val keys = ids.mkString("[\"", "\",\"", "\"]")
      val uriHead = Uri("_all_docs")

      val parameters = Map(
        "keys" -> keys,
        "include_docs" -> "true"
      )

      val uri = uriHead.withQuery(withStale(parameters, stale))

      getDocs(uri.toString(), timeout)
    }
  }

  override def keysWithIds(ids: Seq[String], timeout: Timeout, stale: Boolean): Seq[String] = {
    if (ids.isEmpty) {
      Seq()
    }
    else {
      val keys = ids.mkString("[\"", "\",\"", "\"]")
      val uriHead = Uri("_all_docs")

      val parameters = Map(
        "keys" -> keys,
        "include_docs" -> "false"
      )

      val uri = uriHead.withQuery(withStale(parameters, stale))
      getJsValue(uri.toString(), timeout) match {
        case result: JsObject =>
          result.getFields("rows") match {
            case Seq(rows: JsArray) =>
              rows.elements.flatMap {
                case row: JsObject =>
                  row.getFields("id") match {
                    case Seq(id: JsString) => Some(id.value)
                    case _ => None
                  }
                case _ => deserializationError("'id' field expected")
              }
            case _ => deserializationError("'rows' field expected")
          }
        case _ => deserializationError("JsObject expected")
      }
    }
  }

  private def getDocs(request: String, timeout: Timeout): Seq[JsValue] = {
    getJsValue(request, timeout) match {
      case result: JsObject =>
        result.getFields("rows") match {
          case Seq(rows: JsArray) =>
            rows.elements.flatMap {
              case row: JsObject =>
                row.getFields("doc") match {
                  case Seq(doc: JsObject) => Some(doc)
                  case _ => None
                }
              case _ => deserializationError("'doc' field expected")
            }
          case _ => deserializationError("'rows' field expected")
        }
      case _ => deserializationError("JsObject expected")
    }
  }

  override def query(design: Design, view: View, timeout: Timeout, stale: Boolean = true)(args: Any*): Seq[JsValue] = {
    val uriHead = Uri(s"_design/${design.name}/_view/${view.name}")
    val uri = if (args.isEmpty) {
      val parameters = Map(
        "reduce" -> "false"
      )
      uriHead.withQuery(withStale(parameters, stale))
    }
    else {
      val formattedArgs = args.map {
        case string: String => s""""$string""""
        case other => other.toString
      }
      val startKey = formattedArgs.mkString("[", ",", "]")
      val endKey = (formattedArgs :+ "{}").mkString("[", ",", "]")

      val parameters = Map(
        "startkey" -> startKey,
        "endkey" -> endKey,
        "reduce" -> "false"
      )
      uriHead.withQuery(withStale(parameters, stale))
    }

    getRows(uri.toString(), timeout)
  }

  override def pagingQuery(
    design: Design,
    view: View,
    timeout: Timeout,
    stale: Boolean = true,
    limit: Int,
    skip: Int
  ): PagingQueryResult = {
    val uriHead = Uri(s"_design/${design.name}/_view/${view.name}")
    val parameters = Map(
      "reduce" -> "false",
      "limit" -> limit.toString,
      "skip" -> skip.toString
    )
    val uri = uriHead.withQuery(withStale(parameters, stale))
    getJsValue(uri.toString, timeout) match {
      case obj: JsObject =>
        val totalRows = obj.getFields("total_rows") match {
          case Seq(JsNumber(bigDecimal)) => bigDecimal.toInt
          case _ => deserializationError("'total_rows' field expected")
        }
        val offset = obj.getFields("offset") match {
          case Seq(JsNumber(bigDecimal)) => bigDecimal.toInt
          case _ => deserializationError("'offset' field expected")
        }
        val rows = obj.getFields("rows") match {
          case Seq(array: JsArray) => array.elements
          case _ => Seq()
        }
        PagingQueryResult(totalRows, offset, rows)

      case _ => deserializationError("'rows' field expected")
    }
  }

  override def groupQuery(groupLevel: Int, design: Design, view: View, timeout: Timeout, stale: Boolean = true)(args: Any*): Seq[JsValue] = {
    val uriHead = Uri(s"_design/${design.name}/_view/${view.name}")
    val uri = if (args.isEmpty) {
      val parameters = Map(
        "group" -> "true",
        "group_level" -> groupLevel.toString
      )
      uriHead.withQuery(withStale(parameters, stale))
    }
    else {
      val formattedArgs = args.map {
        case string: String => s""""$string""""
        case other => other.toString
      }
      val startKey = formattedArgs.mkString("[", ",", "]")
      val endKey = (formattedArgs :+ "{}").mkString("[", ",", "]")

      val parameters = Map(
        "startkey" -> startKey,
        "endkey" -> endKey,
        "group" -> "true",
        "group_level" -> groupLevel.toString
      )

      uriHead.withQuery(withStale(parameters, stale))
    }

    getRows(uri.toString(), timeout)
  }

  override def docs(startKey: String, endKey: String, timeout: Timeout, limit: Int): Seq[JsValue] = {
    val uri = Uri("_all_docs")
    val xx = uri.withQuery(
      Query(
        "startkey" -> s""""$startKey"""",
        "endkey" -> s""""$endKey"""",
        "limit" -> limit.toString,
        "include_docs" -> "true",
        "reduce" -> "false"
      )
    )
    getRows(xx.toString(), timeout)
  }

  override def keys(startKey: String, endKey: String, timeout: Timeout): Seq[JsValue] = {
    val uri = Uri("_all_docs")
    val request = uri.withQuery(
      Query(
        "startkey" -> s""""$startKey"""",
        "endkey" -> s""""$endKey"""",
        "include_docs" -> "false",
        "reduce" -> "false"
      )
    )
    getRows(request.toString, timeout).map(r => new ViewRow(r).key)
  }

  /**
   * Retrieves the revisions for the documents with given id's.
   *
   * @param ids The id's of documents for which to find the revisions.
   * @return revision map: key = document-id, value = document-revision (no entry for id's for which there is
   *         no document in the database).
   */
  private def revMap(ids: Seq[String], timeout: Timeout): Map[String, String] = {

    val keys = JsObject(
      Map("keys" -> JsArray(ids.map(s => JsString(s)): _*))
    )
    val uri = Uri(docUrl("_all_docs"))
    val entity = HttpEntity(keys.toString())
    val request = HttpRequest(HttpMethods.POST, uri, entity = entity)

    val data = performRequest2(request) { (response: HttpResponse, entityString: String) =>
      if (response.status != StatusCodes.OK) {
        throw new RuntimeException(s"""Bulk document revision request failed: "${uri.toString()}": ${response.status}""")
      }
      entityString.parseJson
    }

    val rows = data match {
      case obj: JsObject =>
        obj.getFields("rows") match {
          case Seq(array: JsArray) =>

            val yy = array.elements
            yy.flatMap { row =>
              row match {
                case rowObject: JsObject =>

                  if (rowObject.getFields("error").nonEmpty) {
                    None
                  }
                  else {
                    rowObject.getFields("id") match {
                      case Seq(jsId) =>

                        val id = jsId.toString().tail.dropRight(1)
                        val jsValue = rowObject.getFields("value")

                        jsValue match {
                          case Seq(valueObj: JsObject) =>
                            val rev = valueObj.getFields("rev").head.toString().tail.dropRight(1)
                            Some(id -> rev)
                          case _ =>
                            deserializationError(s"JsObject 'value' field expected\nresponse=${data.prettyPrint}\nrow=${rowObject.prettyPrint}")
                        }
                      case _ =>
                        deserializationError(s"row 'id' field expected\nresponse=${data.prettyPrint}\nrow=${rowObject.prettyPrint}")
                    }
                  }

                case _ =>
                  deserializationError(s"JsObject 'row' field expected\nresponse=${data.prettyPrint}\nrow=${row.prettyPrint}")
              }
            }
          case _ => Seq()
        }
      case _ => deserializationError(s"'rows' field expected\nresponse=${data.prettyPrint}")
    }

    rows.toMap
  }

  def bulkSave(docs: Seq[JsValue]): Unit = {
    val content = JsObject(
      Map(
        "all_or_nothing" -> JsFalse,
        "docs" -> JsArray(docs: _*)
      )
    )

    val update = Uri(docUrl("_bulk_docs"))
    val entity = HttpEntity(MediaTypes.`application/json`, content.toString())
    val request = HttpRequest(HttpMethods.POST, update, entity = entity)

    performRequest2(request) { (response: HttpResponse, entityString: String) =>
      if (response.status != StatusCodes.Created) {
        throw new RuntimeException(s"""Update Request failed: "${request.uri}": ${response.status}""")
      }
    }
  }

  private def docUrl(id: String): String = {
    val host = couch.config.host
    val port = couch.config.port
    s"http://$host:$port/$name/$id"
  }

  private def dbUrl: String = {
    val user = couch.config.user
    val password = couch.config.password
    val host = couch.config.host
    val port = couch.config.port
    s"http://$user:$password@$host:$port/$name"
  }

  private def withStale(parameters: Map[String, String], stale: Boolean): Query = {
    val map = if (stale) {
      parameters + ("stale" -> "ok")
    }
    else {
      parameters
    }
    Query(map.seq)
  }

  private def performRequest(request: HttpRequest): String = {
    performRequest2(request) { (response: HttpResponse, entityString: String) =>
      response.status match {
        case StatusCodes.Success(_) => entityString
        case status: StatusCode => throw new RuntimeException(s"""Request failed: "${request.uri}": $status - ${response.entity}""")
      }
    }
  }

  private def performRequest2[T](request: HttpRequest, timeout: Timeout = Couch.batchTimeout)(action: (HttpResponse, String) => T): T = {
    val resultFuture: Future[T] = (
      for {
        response <- Http(system).singleRequest(request)
        text <- {
          Unmarshal(response.entity).to[String].map { entityString =>
            action(response, entityString)
          }
        }
      } yield text
      ).recover {
      case ex =>
        throw new RuntimeException(s"""Request failed: "${request.uri}"\n${ex.getMessage}""", ex)
    }
    Await.result(resultFuture, timeout.duration)
  }

}
