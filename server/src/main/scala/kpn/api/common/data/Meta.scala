package kpn.api.common.data

import kpn.api.custom.Timestamp

trait Meta {
  def version: Int

  def changeSetId: Long

  def timestamp: Timestamp
}
