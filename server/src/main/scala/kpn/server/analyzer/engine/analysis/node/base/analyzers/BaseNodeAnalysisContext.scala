package kpn.server.analyzer.engine.analysis.node.base.analyzers

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NodeName
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Day
import kpn.server.analyzer.engine.context.PreconditionMissingException

case class BaseNodeAnalysisContext(
  node: RawNode,
  active: Boolean = true,
  facts: Seq[Fact] = Seq.empty,
  _name: Option[Option[String]] = None,
  _names: Option[Seq[NodeName]] = None,
  _country: Option[Option[Country]] = None,
  _lastSurvey: Option[Option[Day]] = None,
  _locations: Option[Seq[String]] = None,
  _tiles: Option[Seq[String]] = None,
) {

  def name: Option[String] = _name.getOrElse(throw new PreconditionMissingException)

  def names: Seq[NodeName] = _names.getOrElse(throw new PreconditionMissingException)

  def country: Option[Country] = _country.getOrElse(throw new PreconditionMissingException)

  def lastSurvey: Option[Day] = _lastSurvey.getOrElse(throw new PreconditionMissingException)

  def locations: Seq[String] = _locations.getOrElse(throw new PreconditionMissingException)

  def tiles: Seq[String] = _tiles.getOrElse(throw new PreconditionMissingException)
}
