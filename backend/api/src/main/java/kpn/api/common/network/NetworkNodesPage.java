package kpn.api.common.network;

import kpn.api.common.SurveyDateInfo;
import kpn.api.common.TimeInfo;
import kpn.api.common.network.NetworkNodeRow;
import kpn.api.common.network.NetworkSummary;

import com.google.common.collect.ImmutableList;

public record NetworkNodesPage(
  TimeInfo timeInfo,
  SurveyDateInfo surveyDateInfo,
  NetworkSummary summary,
  ImmutableList<NetworkNodeRow> nodes
) {
}

/*
package kpn.api.common.network

import kpn.api.common.SurveyDateInfo
import kpn.api.common.TimeInfo

case class NetworkNodesPage(
  timeInfo: TimeInfo,
  surveyDateInfo: SurveyDateInfo,
  summary: NetworkSummary,
  nodes: Seq[NetworkNodeRow]
)

*/
