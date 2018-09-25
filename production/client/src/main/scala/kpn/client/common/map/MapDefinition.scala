package kpn.client.common.map

import org.scalajs.dom

trait MapDefinition {

  /*
    The id of the dom element on which the map will be rendered.
   */
  def targetElementId: String = "map"

  /*
    The logic to be executed once the component that contains the dom element
    on which the map will be rendered has been mounted.
   */
  def onMounted(): Unit = {}

  /*
    The OpenLayers map object.
   */
  def map: ol.Map

  /*
    Returns the layers to be shown in the layer switcher popup menu in the upper right
    corner of the map. Returns an empty collection if no layer popup menu should be shown.
  */
  def layers: Seq[ol.layer.Base] = Seq.empty

  def updateTarget(): Unit = {
    val element = dom.document.getElementById(targetElementId)
    if (element != null) {
      map.setTarget(element)
    }
  }
}
