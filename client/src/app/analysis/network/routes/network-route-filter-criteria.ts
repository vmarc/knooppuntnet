import { SurveyDateFilterKind } from '@app/kpn/filter';
import { TimestampFilterKind } from '@app/kpn/filter';

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
