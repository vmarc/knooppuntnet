package kpn.server.analyzer.engine.analysis.network

import kpn.shared.data.Relation

trait NetworkRelationAnalyzer {
  def analyze(relation: Relation): NetworkRelationAnalysis
}
