// TODO migrate to Angular
package kpn.client.components.menu

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Nls.nls
import kpn.client.common.User
import kpn.client.common.Version
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.PageProps
import scalacss.ScalaCssReact._

object UiSidebarFooter {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val footer: StyleA = style(
      borderTopWidth(1.px),
      borderTopStyle.solid,
      borderTopColor.lightgray,
      textAlign.center
    )

    val version: StyleA = style(
      color.lightgray
    )
  }

  private val component = ScalaComponent.builder[PageProps]("sidebar-footer")
    .render_P { props =>
      implicit val context = props.context

      val logoutLogin = User.get match {
        case Some(user) =>
          <.p(
            user,
            <.br(),
            context.gotoLogout()
          )
        case _ =>
          <.p(context.gotoLogin())
      }

      <.div(
        Styles.footer,
        <.p(
          context.gotoAbout(),
          " | ",
          context.gotoGlossary(),
          " | ",
          context.gotoLinks()
        ),
        <.p(
          context.gotoOverview()
        ),
        <.p(
          context.gotoEn(),
          " | ",
          context.gotoNl()
        ),
        <.p(
          <.a(
            ^.href := "https://www.openstreetmap.org/message/new/vmarc",
            ^.cls := "external",
            ^.target := "_blank",
            "Contact"
          ),
          " | ",
          <.a(
            ^.href := "https://github.com/vmarc/knooppuntnet/issues",
            ^.cls := "external",
            ^.target := "_blank",
            "Issues"
          )
        ),
        <.p(
          Styles.version,
          "v" + Version.current
        ),
        logoutLogin
      )
    }
    .build

  def apply[T](props: PageProps): VdomElement = {
    component(props)
  }
}
