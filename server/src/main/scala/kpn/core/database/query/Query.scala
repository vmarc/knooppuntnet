package kpn.core.database.query

import kpn.core.database.views.common.Design
import kpn.core.database.views.common.View

import scala.collection.mutable.ArrayBuffer

object Query {

  def apply[T](designName: String, viewName: String, docType: Class[T]): Query[T] = {
    val url = s"_design/$designName/_view/$viewName"
    Query(url, docType)
  }

  def apply[T](design: Design, view: View, docType: Class[T]): Query[T] = {
    val url = s"_design/${design.name}/_view/${view.name}"
    Query(url, docType)
  }

}

case class Query[T](
  url: String,
  docType: Class[T],
  args: Option[Seq[Any]] = None,
  startKey: Option[String] = None,
  endKey: Option[String] = None,
  stale: Option[String] = None,
  reduce: Option[String] = None,
  descending: Option[String] = None,
  includeDocs: Option[String] = None,
  groupLevel: Option[Int] = None,
  limit: Option[Int] = None,
  skip: Option[Int] = None
) {

  def reduce(value: Boolean): Query[T] = {
    copy(reduce = Some(value.toString))
  }

  def descending(value: Boolean): Query[T] = {
    copy(descending = Some(value.toString))
  }

  def includeDocs(value: Boolean): Query[T] = {
    copy(includeDocs = Some(value.toString))
  }

  def stale(ok: Boolean): Query[T] = {
    if (ok) copy(stale = Some("ok")) else this
  }

  def keyStartsWith(args: Any*): Query[T] = {
    copy(args = Some(args))
  }

  def startKey(startKey: String): Query[T] = {
    copy(startKey = Some(startKey))
  }

  def endKey(endKey: String): Query[T] = {
    copy(endKey = Some(endKey))
  }

  def limit(rowCount: Int): Query[T] = {
    copy(limit = Some(rowCount))
  }

  def skip(rowCount: Int): Query[T] = {
    copy(skip = Some(rowCount))
  }

  def groupLevel(groupLevel: Int): Query[T] = {
    copy(groupLevel = Some(groupLevel))
  }

  def build(): String = {

    val parameters = ArrayBuffer[String]()

    reduce.foreach(value => parameters.append(s"reduce=$value"))
    descending.foreach(value => parameters.append(s"descending=$value"))
    includeDocs.foreach(value => parameters.append(s"include_docs=$value"))

    args.foreach(argsSeq => {
      if (argsSeq.nonEmpty) {
        val formattedArgs = argsSeq.map {
          case string: String => s""""$string""""
          case other => other.toString
        }
        val startKey = formattedArgs.mkString("[", ",", "]")
        val endKey = (formattedArgs :+ "{}").mkString("[", ",", "]")

        parameters.append(s"startkey=$startKey")
        parameters.append(s"endkey=$endKey")
      }
    })

    startKey.foreach(value => parameters.append(s"startkey=$value"))
    endKey.foreach(value => parameters.append(s"endkey=$value"))
    groupLevel.foreach(level => parameters.append(s"group_level=$level"))
    limit.foreach(rowCount => parameters.append(s"limit=$rowCount"))
    skip.foreach(rowCount => parameters.append(s"skip=$rowCount"))
    stale.foreach(arg => parameters.append(s"stale=$arg"))

    val b = new StringBuilder
    b.append(url)
    if (parameters.nonEmpty) {
      b.append("?")
      b.append(parameters.mkString("&"))
    }
    b.toString()
  }
}
