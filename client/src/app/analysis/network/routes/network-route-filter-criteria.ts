import {SurveyDateFilterKind} from '../../../kpn/filter/survey-date-filter-kind';
import {TimestampFilterKind} from '../../../kpn/filter/timestamp-filter-kind';

export class NetworkRouteFilterCriteria {
  constructor(
    readonly investigate: boolean = null,
    readonly accessible: boolean = null,
    readonly roleConnection: boolean = null,
    readonly relationLastUpdated: TimestampFilterKind = TimestampFilterKind.ALL,
    readonly lastSurvey: SurveyDateFilterKind = SurveyDateFilterKind.ALL,
    readonly proposed: boolean = null
  ) {
  }
}
