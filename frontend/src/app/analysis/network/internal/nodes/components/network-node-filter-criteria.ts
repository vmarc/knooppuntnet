import { SurveyDateFilterKind } from '@app/kpn/filter';
import { TimestampFilterKind } from '@app/kpn/filter';

export class NetworkNodeFilterCriteria {
  constructor(
    readonly definedInNetworkRelation: boolean = null,
    readonly referencedInRoute: boolean = null,
    readonly connection: boolean = null,
    readonly roleConnection: boolean = null,
    readonly integrityCheck: boolean = null,
    readonly integrityCheckFailed: boolean = null,
    readonly lastUpdated: TimestampFilterKind = TimestampFilterKind.all,
    readonly lastSurvey: SurveyDateFilterKind = SurveyDateFilterKind.all,
    readonly proposed: boolean = null
  ) {}
}
