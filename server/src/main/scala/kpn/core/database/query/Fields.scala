package kpn.core.database.query

import kpn.api.custom.Country
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType

object Fields {
  def apply(fields: Seq[String]): Fields = new Fields(fields)
}

class Fields(fields: Seq[String]) {

  def long(index: Int): Long = {
    fields(index).toLong
  }

  def string(index: Int): String = {
    fields(index)
  }

  def int(index: Int): Int = {
    fields(index).toInt
  }

  def double(index: Int): Double = {
    fields(index).toDouble
  }

  def boolean(index: Int): Boolean = {
    fields(index).toBoolean
  }

  def networkType(index: Int): NetworkType = {
    NetworkType.withName(fields(index)).get
  }

  def networkScope(index: Int): NetworkScope = {
    NetworkScope.withName(fields(index)).get
  }

  def country(index: Int): Country = {
    Country.withDomain(fields(index)).get
  }

  def optionalString(index: Int): Option[String] = {
    fields.lift(index)
  }

  def optionalLong(index: Int): Option[Long] = {
    fields.lift(index).map(_.toLong)
  }
}
