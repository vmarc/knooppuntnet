import { SurveyDateFilterKind } from '@app/kpn/filter/survey-date-filter-kind';
import { TimestampFilterKind } from '@app/kpn/filter/timestamp-filter-kind';

export class NetworkRouteFilterCriteria {
  constructor(
    readonly investigate: boolean = null,
    readonly accessible: boolean = null,
    readonly roleConnection: boolean = null,
    readonly relationLastUpdated: TimestampFilterKind = TimestampFilterKind.all,
    readonly lastSurvey: SurveyDateFilterKind = SurveyDateFilterKind.all,
    readonly proposed: boolean = null
  ) {}
}
