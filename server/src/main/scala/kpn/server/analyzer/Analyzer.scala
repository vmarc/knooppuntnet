package kpn.server.analyzer

trait Analyzer {

  def load(): Unit

  def process(): Unit
}
