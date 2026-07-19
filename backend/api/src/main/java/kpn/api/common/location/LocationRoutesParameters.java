package kpn.api.common.location;

import kpn.api.common.Fact;
import kpn.api.common.location.BooleanParameter;
import kpn.api.common.location.LastUpdatedParameter;
import kpn.api.common.location.SurveyParameter;

import java.util.Optional;

public record LocationRoutesParameters(
  Optional<Fact> fact,
  Optional<SurveyParameter> survey,
  Optional<LastUpdatedParameter> lastUpdated,
  Optional<BooleanParameter> proposed,
  Long pageSize,
  Long pageIndex
) {
}

/*
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

*/
