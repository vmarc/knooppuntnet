package kpn.server.analyzer.engine.analysis.network

import kpn.api.common.Relation

trait NetworkRelationAnalyzer {
  def analyze(relation: Relation): NetworkRelationAnalysis
}
