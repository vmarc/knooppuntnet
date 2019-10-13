package kpn.core.db.json

import kpn.server.analyzer.engine.analysis.location.LocationTree
import org.scalatest.FunSuite
import org.scalatest.Matchers
import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat
import spray.json._

class JsonFormatsTest extends FunSuite with Matchers with DefaultJsonProtocol {

  test("spray json does not support constructor default parameters - Seq") {

    case class Example(key: String, seq: Seq[String] = Seq.empty)
    implicit val exampleFormat: RootJsonFormat[Example] = jsonFormat2(Example)
    val jsonAst = """{"key": "example"}""".parseJson

    intercept[DeserializationException] {
      jsonAst.convertTo[Example]
    }.getMessage should equal("Object is missing required member 'seq'")

  }

  test("spray json does not support constructor default parameters - String") {

    case class Example(key: String, string: String = "string")
    implicit val exampleFormat: RootJsonFormat[Example] = jsonFormat2(Example)
    val jsonAst = """{"key": "example"}""".parseJson

    intercept[DeserializationException] {
      jsonAst.convertTo[Example]
    }.getMessage should equal("Object is missing required member 'string'")

  }

  test("spray json _does_ support absence of optional parameters") {

    case class Example(key: String, option: Option[String] = None)
    implicit val exampleFormat: RootJsonFormat[Example] = jsonFormat2(Example)
    val jsonAst = """{"key": "example"}""".parseJson

    val example = jsonAst.convertTo[Example]
    example.key should equal("example")
    example.option should equal(None)
  }

  test("spray json does not support constructor default for Option !!") {

    case class Example(key: String, option: Option[String] = Some("string"))
    implicit val exampleFormat: RootJsonFormat[Example] = jsonFormat2(Example)
    val jsonAst = """{"key": "example"}""".parseJson

    val example = jsonAst.convertTo[Example]
    example.key should equal("example")
    example.option should equal(None) // would have been Some("string") if default parameters were supported
  }

  test("extra properties in json do not harm parsing") {

    case class Example(key: String)
    implicit val exampleFormat: RootJsonFormat[Example] = jsonFormat1(Example)
    val jsonAst = """{"key": "example", "extra": "bla"}""".parseJson

    val example = jsonAst.convertTo[Example]
    example.key should equal("example")
  }

  test("use custom formatter to support default value") {

    case class Child(value: String)
    case class Example(
      key: String,
      string: String = "abc",
      child: Child,
      children: Seq[Child],
      optionalChild: Option[Child],
      optionalChild2: Option[Child], // no default in format
      boolean: Boolean
    )

    implicit val subFormat: RootJsonFormat[Child] = jsonFormat1(Child)

    implicit object ExampleFormat extends RootJsonFormat[Example] {
      override def write(obj: Example): JsValue = JsObject(
        "key" -> obj.key.toJson,
        "string" -> obj.string.toJson,
        "child" -> obj.child.toJson,
        "children" -> obj.children.toJson,
        "optionalChild" -> obj.optionalChild.toJson,
        "optionalChild2" -> obj.optionalChild2.toJson,
        "boolean" -> obj.boolean.toJson
      )

      override def read(json: JsValue): Example = {
        val childDefault: Option[Child] = Some(Child("default2"))
        val childNone: Option[Child] = None
        val fields = json.asJsObject("Invalid Json Object").fields
        Example(
          fields("key").convertTo[String],
          fields.get("string").fold("123")(_.convertTo[String]),
          fields.get("child").fold(Child("default"))(_.convertTo[Child]),
          fields.get("children").fold(Seq[Child]())(_.convertTo[Seq[Child]]),
          fields.get("optionalChild").fold(childDefault)(_.convertTo[Option[Child]]),
          fields.get("optionalChild2").fold(childNone)(_.convertTo[Option[Child]]),
          fields.get("boolean").fold(true)(_.convertTo[Boolean])
        )
      }
    }

    val example1 = Example("1", "2", Child("3"), Seq(Child("4"), Child("5")), Some(Child("6")), Some(Child("7")), boolean = true)
    example1.toJson.convertTo[Example] should equal(example1)

    val jsonAstExample2 = """{"key":"example","extra":"bla"}""".parseJson
    val example2 = jsonAstExample2.convertTo[Example]
    example2.key should equal("example")
    example2.string should equal("123") // note that this is the default value from the format and not the case class parameter
    example2.child should equal(Child("default"))
    example2.children should equal(Seq())
    example2.optionalChild should equal(Some(Child("default2")))
    example2.optionalChild2 should equal(None)
    example2.boolean should equal(true)
  }


  test("can read/write recursive objects") {

    val a = LocationTree("a")
    val b = LocationTree("b")

    val root = LocationTree("root", Seq(a, b))

    val json = JsonFormats.locationTreeFormat.write(root).toString()
    json should equal("""{"name":"root","children":[{"name":"a","children":[]},{"name":"b","children":[]}]}""")

    val read = JsonFormats.locationTreeFormat.read(json.parseJson)
    read should equal(root)
  }

}
