package kpn.client.components.common

object PageStatus extends Enumeration {
  val NotAuthorized, LoadStarting, Loading,
  UpdateStarting, Updating,
  Ready, NotFound, Timeout, Failure = Value
}
