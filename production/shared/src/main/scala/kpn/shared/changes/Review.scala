package kpn.shared.changes

import kpn.shared.Timestamp

case class Review(user: String, timestamp: Timestamp, status: String, comment: String)
