import { TimestampFilterKind } from '@app/shared/kpn/filter/timestamp-filter-kind';

export class SubsetOrphanRouteFilterCriteria {
  constructor(
    readonly broken: boolean = null,
    readonly lastUpdated: TimestampFilterKind = TimestampFilterKind.all
  ) {}
}
