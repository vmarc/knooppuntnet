package kpn.server.analyzer.engine.analysis.network

import kpn.api.custom.Relation

trait NetworkRelationAnalyzer {
  def analyze(relation: Relation): NetworkRelationAnalysis
}
