package kpn.core.doc

case class SuperSegmentElement(relationSegment: SuperSegmentElementInfo, reversed: Boolean = false) {

  def summary: String = s"${relationSegment.id} start=${relationSegment.startNodeId}, end=${relationSegment.endNodeId}, reversed=$reversed"

  def startNodeId: Long = {
    if (reversed) {
      relationSegment.endNodeId
    }
    else {
      relationSegment.startNodeId
    }
  }

  def endNodeId: Long = {
    if (reversed) {
      relationSegment.startNodeId
    }
    else {
      relationSegment.endNodeId
    }
  }
}

