package kpn.shared.data

import kpn.shared.Timestamp

trait Meta {
  def version: Int

  def changeSetId: Long

  def timestamp: Timestamp
}
