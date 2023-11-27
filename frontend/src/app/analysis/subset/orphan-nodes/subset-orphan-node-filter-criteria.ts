import { TimestampFilterKind } from '@app/kpn/filter';

export class SubsetOrphanNodeFilterCriteria {
  constructor(readonly lastUpdated: TimestampFilterKind = TimestampFilterKind.all) {}
}
