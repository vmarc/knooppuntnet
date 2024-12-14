package kpn.api.common.location

import kpn.api.common.Fact

case class LocationNodesParameters(
  integrityCheck: Option[BooleanParameter] = None,
  integrityCheckFailed: Option[BooleanParameter] = None,
  fact: Option[Fact] = None,
  survey: Option[SurveyParameter] = None,
  lastUpdated: Option[LastUpdatedParameter] = None,
  proposed: Option[BooleanParameter] = None,
  referencedInRoutes: Option[BooleanParameter] = None,
  pageSize: Long = 5,
  pageIndex: Long = 0
)
