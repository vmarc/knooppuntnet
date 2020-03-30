// Migrated to Angular
package kpn.client.common

import org.scalajs.dom

object User {

  private var loginCallbackPage: Option[String] = None

  def registerLoginCallbackPage(): Unit = {
    dom.window.location.pathname match {
      case "/login" =>
      case "/en/login" =>
      case "/nl/login" =>
      case path =>
        loginCallbackPage = Some(path)
    }
  }

  def clearLoginCallbackPage(): Unit = {
    loginCallbackPage = None
  }

  def getLoginCallbackPage: Option[String] = {
    loginCallbackPage
  }

  def get: Option[String] = {
    Cookies.cookies.get("knooppuntnet-user")
  }

}
