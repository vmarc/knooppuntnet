// TODO migrate to Angular
package kpn.client.common

import org.scalajs.dom

object BrowserLocalStorage {

  private val defaultItemsPerPage = 25

  def itemsPerPage: Int = {
    val value: String = dom.window.localStorage.getItem("items-per-page")
    if (value != null && value.length > 0) {
      value.toInt
    }
    else {
      itemsPerPage = defaultItemsPerPage
      defaultItemsPerPage
    }
  }

  def itemsPerPage_=(value: Int): Unit = {
    dom.window.localStorage.setItem("items-per-page", value.toString)
  }

  def impact: Boolean = {
    val value: String = dom.window.localStorage.getItem("impact")
    if (value == null) {
      impact = false
      false
    }
    else {
      "true" == value
    }
  }

  def impact_=(value: Boolean): Unit = {
    dom.window.localStorage.setItem("impact", value.toString)
  }

}
