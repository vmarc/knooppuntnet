// TODO migrate to Angular
package kpn.client.common.map.vector

class SelectedFeatureHolder {

  var listener: Option[(Option[SelectedFeature]) => Unit] = None

  def select(selection: Option[SelectedFeature]): Unit = {
    listener.foreach(f => f(selection))
  }
}
