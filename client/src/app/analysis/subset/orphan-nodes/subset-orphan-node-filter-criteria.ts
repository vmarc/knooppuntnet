import { TimestampFilterKind } from '@app/kpn/filter/timestamp-filter-kind';

export class SubsetOrphanNodeFilterCriteria {
  constructor(
    readonly lastUpdated: TimestampFilterKind = TimestampFilterKind.all
  ) {}
}
