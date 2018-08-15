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
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.Nls.nlsEN
import kpn.client.common.Nls.nlsNL
import kpn.client.common.User
import kpn.client.components.common.DefaultBackend
import kpn.client.components.common.DefaultProps
import kpn.client.components.common.PageState
import kpn.client.components.common.UiPageContents
import org.scalajs.dom

import scala.scalajs.js.URIUtils

object UiLoginPage {

  private class Backend(scope: BackendScope[DefaultProps, PageState[Unit]]) extends DefaultBackend(scope) {

    protected def contents(props: DefaultProps, state: PageState[Unit]): VdomElement = {

      implicit val context: Context = props.context
      import scala.concurrent.ExecutionContext.Implicits.global

      val loginCallback = CallbackTo {

        val whereWeComeFrom = User.getLoginCallbackPage match {
          case Some(url) =>
            User.clearLoginCallbackPage()
            url
          case None => "/"
        }

        val loginUrl = "/login?callbackUrl=" + URIUtils.encodeURI(dom.window.location.origin + "/authenticate?page=" + whereWeComeFrom)

        dom.ext.Ajax.get(
          url = loginUrl,
          timeout = 25000
        ).map { r =>
          dom.window.location.href = "https://www.openstreetmap.org/oauth/authorize?oauth_token=" + r.response
        }
        ()
      }

      new Renderer(loginCallback).render()
    }

    protected def title: String = nls("Login", "Login")
  }

  private val component = ScalaComponent.builder[DefaultProps]("login")
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

  private class Renderer(loginCallback: Callback)(implicit context: Context) {

    private val onTouchTapHandler = (event: TouchTapEvent) => {
      event.preventDefault()
      loginCallback
    }

    def render(): VdomElement = {
      <.div(
        <.h1(nls("Login", "Aanmelden")),
        UiPageContents(
          TagMod.when(nlsEN) {
            <.i(
              <.p(
                "Login allows you to view extra information, such as changeset details, network history, route history and node history."
              ),
              <.p(
                "This information is available to registered OpenStreetMap users only."
              ),
              <.p(
                """After clicking the "login" button below, you will be directed (via the OpenStreetMap login page
                  | if you are not logged in to OpenStreetMap yet) to the OpenStreetMap
                  | page "Authorize access to your account". Click the button "Grant access"
                  | (leave the option "read your user preferences" checked).""".stripMargin
              ),
              <.p(
                """The knooppuntnet application only reads your username from the user preferences to establish that
                  | you are a registered OpenStreetMap user. All other user preferences are ignored,
                  | and not stored in knooppuntnet.""".stripMargin
              ),
              <.p(
                "This login procedure uses OpenStreetMap security. Knooppuntnet does not have access to your password at any moment."
              ),
              <.p(
                """You remain logged in (also after closing you browser), until you explicitly logout through the logout page.
                  | To login again after logout you will have to use the same login procedure again.""".stripMargin
              )
            )
          },
          TagMod.when(nlsNL) {
            <.i(
              <.p(
                "Na het aanmelden krijgt u toegang to extra informatie: wijzigingset details, netwerk historiek, ",
                "route historiek en knooppunt historiek."
              ),
              <.p(
                "Deze informatie is enkel beschikbaar voor geregistreerde OpenStreetMap gebruikers."
              ),
              <.p(
                """Na het klikken op de "aanmelden" knop hieronder, wordt u (eventueel via het aanmeldenscherm
                  |van OpenStreetMap zelf) naar de OpenStreetMap pagina "Geef toegang tot uw account" gebracht.
                  |Hier klikt U op de knop "Toegang verlenen" (laat hierbij de optie "uw gebruikersvoorkeuren lezen"
                  |aangevinkt).""".stripMargin
              ),
              <.p(
                """De knooppuntnet toepassing leest enkel uw gebruikersnaam uit uw gebruikervoorkeuren om vast
                  |te stellen dat u een registreerde OpenStreetMap gebruiker bent. De andere informatie in uw
                  |gebruikervoorkeuren wordt niet gebruikt en niet opgeslagen in knooppuntnet.""".stripMargin
              ),
              <.p(
                "Deze inlog procedure loopt via OpenStreetMap, en op geen enkel moment heeft knooppuntnet toegang tot uw paswoord."
              ),
              <.p(
                """U blijft aangemeld bij knooppuntnet (ook na het afsluiten van uw browser) tot u zich
                  |uitdrukkelijk afmeld via het afmelden scherm. Om daarna opnieuw aan te melden dient u
                  |opnieuw dezelfde procedure door te lopen.""".stripMargin
              )
            )
          },
          <.br(),
          <.br(),
          MuiRaisedButton(
            label = nls("Login", "Aanmelden"),
            labelColor = Mui.Styles.colors.white,
            backgroundColor = Mui.Styles.colors.blue500,
            onTouchTap = onTouchTapHandler
          )()
        )
      )
    }
  }

}
