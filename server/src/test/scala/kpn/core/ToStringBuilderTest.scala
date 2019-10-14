package kpn.core

import kpn.shared.common.ToStringBuilder
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ToStringBuilderTest extends FunSuite with Matchers {

  case class Example(a: String, b: String) {
    override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
      field("a", a).
      field("b", b).
      build
  }

  case class ExampleCollection(examples: Seq[Example]) {
    override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
      optionalCollection("examples", examples).
      build
  }

  ignore("regular fields") {

    val string = Example("aaa", "bbb").toString

    string should equal(
      """|Example(
         |  a = aaa
         |  b = bbb
         |)""".stripMargin
    )
  }

  ignore("collection") {

    val string = ExampleCollection(
      Seq(
        Example("1", "one"),
        Example("2", "two")
      )
    ).toString

    string should equal(
      """|ExampleCollection(
         |  examples = Seq(
         |    Example(
         |      a = 1
         |      b = one
         |    ),
         |    Example(
         |      a = 2
         |      b = two
         |    )
         |  )
         |)""".stripMargin
    )
  }
}
