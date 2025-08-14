package kpn.core.doc

case class SuperSubSegment(info: SuperSubSegmentInfo, reversed: Boolean = false) extends Storable {

  def startNodeId: Long = {
    if (reversed) {
      info.endNodeId
    }
    else {
      info.startNodeId
    }
  }

  def endNodeId: Long = {
    if (reversed) {
      info.startNodeId
    }
    else {
      info.endNodeId
    }
  }
}

