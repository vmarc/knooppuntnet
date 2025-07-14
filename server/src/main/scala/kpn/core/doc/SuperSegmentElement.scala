package kpn.core.doc

case class SuperSegmentElement(elementInfo: SuperSegmentElementInfo, reversed: Boolean = false) {

  def startNodeId: Long = {
    if (reversed) {
      elementInfo.endNodeId
    }
    else {
      elementInfo.startNodeId
    }
  }

  def endNodeId: Long = {
    if (reversed) {
      elementInfo.startNodeId
    }
    else {
      elementInfo.endNodeId
    }
  }
}

