package kpn.api.common.network;

import kpn.api.common.RouteType;
import kpn.api.common.SurveyDateInfo;
import kpn.api.common.TimeInfo;
import kpn.api.common.network.NetworkRouteRow;
import kpn.api.common.network.NetworkSummary;

import com.google.common.collect.ImmutableList;

public record NetworkRoutesPage(
  TimeInfo timeInfo,
  SurveyDateInfo surveyDateInfo,
  RouteType routeType,
  NetworkSummary summary,
  ImmutableList<NetworkRouteRow> routes
) {
}
