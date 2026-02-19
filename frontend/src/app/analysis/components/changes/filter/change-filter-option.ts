import { ChangesFilterPeriod } from '@api/common/changes/filter/changes-filter-period';

export class ChangeFilterOption {
  constructor(
    readonly level: string,
    readonly period: ChangesFilterPeriod,
    readonly options: ReadonlyArray<ChangeFilterOption>,
    readonly impactedCountClicked: () => void,
    readonly totalCountClicked: () => void
  ) {}
}
