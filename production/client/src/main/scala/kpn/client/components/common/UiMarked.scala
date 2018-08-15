package kpn.client.components.common

import com.karasiq.markedjs.Marked
import com.karasiq.markedjs.MarkedOptions
import com.karasiq.markedjs.MarkedRenderer
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^

object UiMarked {

  private val renderTable: (String, String) => String = { (header: String, body: String) =>
    s"""
       |<table class="ui celled collapsing compact unstackable table">
       |<thead>
       |$header
       |</thead>
       |<tbody>
       |$body
       |</tbody>
       |</table>
        """.stripMargin
  }

  private val options = MarkedOptions(
    renderer = MarkedRenderer(
      table = renderTable
    )
  )

  private case class Props(text: String, paragraphs: Boolean)

  private val component = ScalaComponent.builder[Props]("marked")
    .render_P { props =>
      val marked = Marked(props.text, options)
      val innerHtml = if (props.paragraphs) {
        marked
      }
      else {
        marked.replaceAll("<p>", "").replaceAll("</p>", "")
      }
      <.span(^.dangerouslySetInnerHtml := innerHtml)
    }
    .build

  def apply(text: String, paragraphs: Boolean = true): VdomElement = component(Props(text, paragraphs))
}
