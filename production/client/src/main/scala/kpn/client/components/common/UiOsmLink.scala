// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^

object UiOsmLink {

  def node(id: Long): VdomElement = link("node", id)

  def osmNode(id: Long): VdomElement = osmLink("node", id)

  def way(id: Long): VdomElement = link("way", id)

  def osmWay(id: Long): VdomElement = osmLink("way", id)

  def relation(id: Long): VdomElement = link("relation", id)

  def osmRelation(id: Long): VdomElement = osmLink("relation", id)

  def user(id: String): VdomElement = linkString("user", id)

  def changeSet(id: String): VdomElement = linkString("changeset", id)

  def message(id: String): VdomElement = linkString("message/new", id)

  def changeSet(id: Long): VdomElement = link("changeset", id)

  def osmChangeSet(id: Long): VdomElement = osmLink("changeset", id)

  def josmNode(id: Long): VdomElement = josmLink("node", id)

  def josmWay(id: Long): VdomElement = josmLink("way", id)

  def josmRelation(id: Long): VdomElement = josmLink("relation", id, full = true)

  def link(kind: String, id: Long): VdomElement = {
    link(kind, id, "osm")
  }

  def link(kind: String, id: Long, title: String): VdomElement = {
    <.a(
      ^.cls := "external",
      ^.href := s"https://www.openstreetmap.org/$kind/$id",
      ^.rel := "nofollow",
      ^.target := "_blank",
      title
    )
  }

  def oathClients(user: String, title: String): VdomElement = {
    <.a(
      ^.cls := "external",
      ^.href := s"https://www.openstreetmap.org/user/$user/oauth_clients",
      ^.rel := "nofollow",
      ^.target := "_blank",
      title
    )
  }

  def osmLink(kind: String, id: Long): VdomElement = {
    link(kind, id, id.toString)
  }

  private def linkString(kind: String, id: String): VdomElement = {
    <.a(
      ^.cls := "external",
      ^.href := s"https://www.openstreetmap.org/$kind/$id",
      ^.rel := "nofollow",
      ^.target := "_blank",
      id
    )
  }

  private def josmLink(kind: String, id: Long, full: Boolean = false): VdomElement = {
    val suffix = if (full) "/full" else ""
    <.a(
      ^.href := s"http://localhost:8111/import?url=https://api.openstreetmap.org/api/0.6/$kind/$id$suffix",
      ^.rel := "nofollow",
      "edit"
    )
  }
}
