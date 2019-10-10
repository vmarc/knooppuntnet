package kpn.core.db

import kpn.shared.changes.Review

case class ReviewDoc(_id: String, reviews: Seq[Review], _rev: Option[String] = None)
