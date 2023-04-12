import { TimestampFilterKind } from '@app/kpn/filter';

export class SubsetOrphanRouteFilterCriteria {
  constructor(
    readonly broken: boolean = null,
    readonly lastUpdated: TimestampFilterKind = TimestampFilterKind.all
  ) {}
}
