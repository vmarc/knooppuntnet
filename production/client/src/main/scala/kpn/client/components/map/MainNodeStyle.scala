// TODO migrate to Angular
package kpn.client.components.map

import kpn.client.common.map.vector.MapState

import scala.scalajs.js

object MainNodeStyle {

  private val largeMinZoomLevel = 13
  private val yellow = ol.Color(255, 255, 0)
  private val white = ol.Color(255, 255, 255)

  private val smallNodeSelectedStyle = ol.style.Style(
    image = ol.style.Circle(
      radius = 8,
      fill = ol.style.Fill(
        color = yellow
      )
    )
  )

  private val largeNodeSelectedStyle = ol.style.Style(
    image = ol.style.Circle(
      radius = 20,
      fill = ol.style.Fill(
        color = yellow
      )
    )
  )

  private val largeNodeStyle = ol.style.Style(
    image = ol.style.Circle(
      radius = 14,
      fill = ol.style.Fill(
        color = white
      ),
      stroke = ol.style.Stroke(
        color = MainStyleColors.green,
        width = 3
      )
    ),
    text = ol.style.Text(
      text = "",
      textAlign = "center",
      textBaseline = "middle",
      font = "14px Arial, Verdana, Helvetica, sans-serif",
      stroke = ol.style.Stroke(
        color = white,
        width = 5
      )
    )
  )

  private val smallNodeStyle = ol.style.Style(
    image = ol.style.Circle(
      radius = 3,
      fill = ol.style.Fill(
        color = white
      ),
      stroke = ol.style.Stroke(
        color = MainStyleColors.green,
        width = 2
      )
    )
  )

  private val smallNodeStyleError = ol.style.Style(
    image = ol.style.Circle(
      radius = 3,
      fill = ol.style.Fill(
        color = white
      ),
      stroke = ol.style.Stroke(
        color = MainStyleColors.blue,
        width = 2
      )
    )
  )

  private val smallNodeStyleDisabled = ol.style.Style(
    image = ol.style.Circle(
      radius = 3,
      fill = ol.style.Fill(
        color = white
      ),
      stroke = ol.style.Stroke(
        color = MainStyleColors.gray,
        width = 2
      )
    )
  )

  private val smallNodeStyleOrphan = ol.style.Style(
    image = ol.style.Circle(
      radius = 3,
      fill = ol.style.Fill(
        color = white
      ),
      stroke = ol.style.Stroke(
        color = MainStyleColors.darkGreen,
        width = 2
      )
    )
  )

  private val smallNodeStyleErrorOrphan = ol.style.Style(
    image = ol.style.Circle(
      radius = 3,
      fill = ol.style.Fill(
        color = white
      ),
      stroke = ol.style.Stroke(
        color = MainStyleColors.darkBlue,
        width = 2
      )
    )
  )

  def createNodeStyle(state: MapState, zoom: Int, feature: ol.render.Feature, enabled: Boolean = true): js.Array[ol.style.Style] = {

    val featureId = feature.get("id").toString
    val layer = feature.get("layer").toString

    val selectedStyle = if (state.selectedNodeId.isDefined && featureId.nonEmpty && feature.get("id").toString == state.selectedNodeId.get) {
      if (zoom >= largeMinZoomLevel) {
        Some(largeNodeSelectedStyle)
      }
      else {
        Some(smallNodeSelectedStyle)
      }
    }
    else {
      None
    }

    val style = if (zoom >= largeMinZoomLevel) {

      val nodeColor = if (enabled) {
        layer match {
          case "error-node" => MainStyleColors.blue
          case "orphan-node" => MainStyleColors.darkGreen
          case "error-orphan-node" => MainStyleColors.darkBlue
          case _ => MainStyleColors.green
        }
      }
      else {
        MainStyleColors.gray
      }

      largeNodeStyle.getText().setText(feature.get("name").toString)
      largeNodeStyle.getImage().asInstanceOf[ol.style.Circle].getStroke().setColor(nodeColor)

      if (state.highlightedNodeId.isDefined && feature.get("id").toString == state.highlightedNodeId.get) {
        largeNodeStyle.getImage().asInstanceOf[ol.style.Circle].getStroke().setWidth(5)
        largeNodeStyle.getImage().asInstanceOf[ol.style.Circle].setRadius(16)
      }
      else {
        largeNodeStyle.getImage().asInstanceOf[ol.style.Circle].getStroke().setWidth(3)
        largeNodeStyle.getImage().asInstanceOf[ol.style.Circle].setRadius(14)
      }
      largeNodeStyle
    }
    else {
      if (enabled) {
        layer match {
          case "error-node" =>  smallNodeStyleError
          case "orphan-node" => smallNodeStyleOrphan
          case "error-orphan-node" => smallNodeStyleErrorOrphan
          case _ => smallNodeStyle
        }
      }
      else {
        smallNodeStyleDisabled
      }
    }

    selectedStyle match {
      case Some(ss) => js.Array(ss, style)
      case None => js.Array(style)
    }
  }

}
