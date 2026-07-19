package kpn.api.common

import kpn.api.common.changes.details.ChangeKey

case class LocationChangeSet(
  _id: String,
  key: ChangeKey,
  locationChanges: Seq[LocationChanges]
) {

  def happy: Boolean = {
    locationChanges.exists { locationChanges =>
      locationChanges.happy
    }
  }

  def investigate: Boolean = {
    locationChanges.exists { locationChanges =>
      locationChanges.investigate
    }
  }
}
