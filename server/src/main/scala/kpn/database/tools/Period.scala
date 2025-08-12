package kpn.database.tools

import kpn.core.doc.Storable

// TODO scala3 move back into using class
case class Period(
  year: Option[Long],
  month: Option[Long],
  day: Option[Long],
  count: Long
) extends Storable
