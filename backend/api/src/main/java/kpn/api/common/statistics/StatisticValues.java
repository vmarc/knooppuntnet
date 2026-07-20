package kpn.api.common.statistics;

import kpn.api.common.statistics.StatisticValue;

import com.google.common.collect.ImmutableList;

public record StatisticValues(
  String _id,
  String total,
  ImmutableList<StatisticValue> values
) {}
