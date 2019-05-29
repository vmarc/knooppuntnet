// TODO migrate to Angular
package kpn.client.components.login

import chandu0101.scalajs.react.components.materialui.Mui
import chandu0101.scalajs.react.components.materialui.MuiRaisedButton
import chandu0101.scalajs.react.components.materialui.TouchTapEvent
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.Nls.nlsEN
import kpn.client.common.Nls.nlsNL
import kpn.client.common.User
import kpn.client.components.common.DefaultBackend
import kpn.client.components.common.DefaultProps
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.UiOsmLink
import kpn.client.components.common.UiPageContents
import org.scalajs.dom

object UiLogoutPage {

  private class Backend(scope: BackendScope[DefaultProps, PageState[Unit]]) extends DefaultBackend(scope) {

    protected def contents(props: DefaultProps, state: PageState[Unit]): VdomElement = {

      implicit val context: Context = props.context
      import scala.concurrent.ExecutionContext.Implicits.global

      val logoutCallback = CallbackTo {
        updatePageState(state.copy(ui = state.ui.copy(status = PageStatus.Updating)))
        dom.ext.Ajax.get(
          url = "/logout",
          timeout = 25000 // TODO reduce timeout value ???
        ).map { r =>
          updatePageState(state.copy(ui = state.ui.copy(status = PageStatus.Ready)))
        }
        ()
      }

      new Renderer(logoutCallback, state.ui.status).render()
    }

    protected def title: String = nls("Logout", "Logout")
  }

  private val component = ScalaComponent.builder[DefaultProps]("logout-page")
    .initialState(PageState.ready)
    .renderBackend[Backend]
    .componentDidMount { scope =>
      Callback {
        scope.backend.installResizeListener()
      }
    }
    .componentWillUnmount { scope =>
      scope.backend.removeResizeListener()
    }
    .build

  def apply(context: Context): VdomElement = component(DefaultProps(context))

  private class Renderer(logoutCallback: Callback, status: PageStatus.Value)(implicit context: Context) {

    private val onTouchTapHandler = (event: TouchTapEvent) => {
      event.preventDefault()
      logoutCallback
    }

    def render(): VdomElement = {
      <.div(
        <.h1(nls("Logout", "Afmelden")),
        UiPageContents(
          status match {
            case PageStatus.Updating => updating()
            case PageStatus.Ready =>
              User.get match {
                case Some(user) => loggedIn(user)
                case None => notLoggedIn()
              }
            case _ => <.div("?")
          }
        )
      )
    }

    private def loggedIn(user: String): TagMod = {
      <.div(
        TagMod.when(nlsEN) {
          enLoggedIn(user)
        },
        TagMod.when(nlsNL) {
          nlLoggedIn(user)
        },
        <.br(),
        <.br(),
        MuiRaisedButton(
          label = nls("Logout", "Afmelden"),
          labelColor = Mui.Styles.colors.white,
          backgroundColor = Mui.Styles.colors.blue500,
          onTouchTap = onTouchTapHandler
        )()
      )
    }

    private def enLoggedIn(user: String): TagMod = {
      <.div(
        <.p(
          "You are currently logged in as ",
          UiOsmLink.user(user),
          "."
        ),
        <.br(),
        <.i(
          <.p(
            "This allows you to view extra information that is available to OpenStreetMap users only."
          ),
          <.p(
            "After logging out you can continue to use this website. However, the additional information such as changesets, ",
            "network changes, route changes and node changes will not be visible anymore. ",
            "Your personal OpenStreetMap ",
            UiOsmLink.oathClients(user, "list of authorised applications"),
            " will still contain this application. The application can safely be revoked. ",
            " The authorization will not be used anymore. A new authorization will be created when logging in again."
          )
        )
      )
    }

    private def nlLoggedIn(user: String): TagMod = {
      <.div(
        <.p(
          "U bent momenteel aangemeld als ",
          UiOsmLink.user(user),
          "."
        ),
        <.p(
          "Dit laat u toe om extra informatie te raadplegen die enkel beschikbaar is voor geregistreerde OpenStreetMap gebruikers."
        ),
        <.p(
          "Na het uitloggen kan u de website verder gebruiken, maar extra informatie zoals wijzigingset details, ",
          "netwerk historiek, route historiek en knooppunt historiek zijn niet meer raadpleegbaar. ",
          "In uw persoonlijke ",
          UiOsmLink.oathClients(user, "lijst met geautoriseerde applicaties"),
          " zal knooppuntnet nog wel voorkomen. U kunt deze autorisatie intrekken. ",
          "De autorisatie zal niet meer gebruikt worden. ",
          "Een nieuwe autorisatie zal aangemaakt worden bij opnieuw aanmelden."
        )
      )
    }

    private def updating(): TagMod = {
      <.div(
        nls(
          "Requesting server to log out...",
          "Aanvraag tot afmelden verstuurd naar server..."
        )
      )
    }

    private def notLoggedIn(): TagMod = {
      <.div(
        TagMod.when(nlsEN) {
          <.div(
            <.p("You are not logged in."),
            <.p(
              "You can ",
              context.gotoLogin(),
              " to view extra information that is available to ",
              <.a(
                ^.cls := "external",
                ^.href := "https://openstreetmap.org",
                ^.target := "_blank",
                "OpenStreetMap"
              ),
              " users only."
            )
          )
        },
        TagMod.when(nlsNL) {
          <.div(
            <.p("U bent niet aangemeld."),
            <.p(
              " Ga naar ",
              context.gotoLogin(),
              " om toegang te krijgen tot extra informatie die enkel beschikbaar is voor ",
              <.a(
                ^.cls := "external",
                ^.href := "https://openstreetmap.org",
                ^.target := "_blank",
                "OpenStreetMap"
              ),
              " gebruikers."
            )
          )
        }
      )
    }
  }

}
