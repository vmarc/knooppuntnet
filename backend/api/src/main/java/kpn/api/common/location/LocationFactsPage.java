package kpn.api.common.location;

import kpn.api.common.location.LocationFact;
import kpn.api.common.location.LocationSummary;

import com.google.common.collect.ImmutableList;

public record LocationFactsPage(
  LocationSummary summary,
  ImmutableList<LocationFact> locationFacts
) {}
