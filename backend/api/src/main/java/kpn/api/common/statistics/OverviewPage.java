package kpn.api.common.statistics;

import kpn.api.common.statistics.StatisticValues;

import com.google.common.collect.ImmutableList;

public record OverviewPage(
  ImmutableList<StatisticValues> values
) {}

