package kpn.api.common.location

import kpn.api.common.Fact

case class LocationRoutesParameters(
  fact: Option[Fact] = None,
  survey: Option[SurveyParameter] = None,
  lastUpdated: Option[LastUpdatedParameter] = None,
  proposed: Option[BooleanParameter] = None,
  pageSize: Long = 5,
  pageIndex: Long = 0
)
