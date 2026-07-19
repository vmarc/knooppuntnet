package kpn.api.common;

import kpn.api.common.LocationChanges;
import kpn.api.common.changes.details.ChangeKey;

import com.google.common.collect.ImmutableList;

public record LocationChangeSet(
  String _id,
  ChangeKey key,
  ImmutableList<LocationChanges> locationChanges
) {
}

/*
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

*/
