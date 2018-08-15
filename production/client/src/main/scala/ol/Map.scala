package ol

import org.scalajs.dom._

import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation._
import scala.scalajs.js.|

object Map {
  def apply(
    controls: UndefOr[ol.Collection[ol.control.Control] /*| js.Array[ol.control.Control]*/ ] = js.undefined,
    layers: Seq[ol.layer.Base],
    target: UndefOr[String] = js.undefined,
    view: ol.View
  ): Map = {
    new Map(
      olx.MapOptions(
        controls = controls,
        layers = layers,
        target = target,
        view = view
      )
    )
  }
}

@js.native
@JSGlobal("ol.Map")
class Map protected () extends ol.Object {

  def this(options: olx.MapOptions) = this()

  def addInteraction(interaction: ol.interaction.Interaction): Unit = js.native

  def addLayer(layer: ol.layer.Base): Unit = js.native

  def getTargetElement(): Element = js.native

  def getLayers(): ol.Collection[ol.layer.Base] = js.native

  def getView(): ol.View = js.native

  def render(): Unit = js.native

  def setTarget(target: Element | String): Unit = js.native
}
