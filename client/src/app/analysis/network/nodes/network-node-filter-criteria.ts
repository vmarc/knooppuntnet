import {SurveyDateFilterKind} from "../../../kpn/filter/survey-date-filter-kind";
import {TimestampFilterKind} from "../../../kpn/filter/timestamp-filter-kind";

export class NetworkNodeFilterCriteria {

  constructor(readonly definedInNetworkRelation: boolean = null,
              readonly definedInRouteRelation: boolean = null,
              readonly referencedInRoute: boolean = null,
              readonly connection: boolean = null,
              readonly roleConnection: boolean = null,
              readonly integrityCheck: boolean = null,
              readonly integrityCheckFailed: boolean = null,
              readonly lastUpdated: TimestampFilterKind = TimestampFilterKind.ALL,
              readonly lastSurvey: SurveyDateFilterKind = SurveyDateFilterKind.ALL) {
  }

}
