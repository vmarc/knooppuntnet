package kpn.api.common;

import kpn.api.custom.Subset;

public record ChangeSetSubsetAnalysis(
  Subset subset,
  Boolean happy,
  Boolean investigate
) {}
