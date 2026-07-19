package kpn.api.common.location;

import kpn.api.common.Fact;
import kpn.api.common.location.BooleanParameter;
import kpn.api.common.location.LastUpdatedParameter;
import kpn.api.common.location.SurveyParameter;

import java.util.Optional;

public record LocationNodesParameters(
  Optional<BooleanParameter> integrityCheck,
  Optional<BooleanParameter> integrityCheckFailed,
  Optional<Fact> fact,
  Optional<SurveyParameter> survey,
  Optional<LastUpdatedParameter> lastUpdated,
  Optional<BooleanParameter> proposed,
  Optional<BooleanParameter> referencedInRoutes,
  Long pageSize,
  Long pageIndex
) {
}

/*
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

*/
