import { ChangesFilterPeriod } from '@api/common/changes/filter/changes-filter-period';
import { List } from 'immutable';

export class ChangeFilterOption {
  constructor(
    readonly level: string,
    readonly period: ChangesFilterPeriod,
    readonly options: List<ChangeFilterOption>,
    readonly impactedCountClicked: () => void,
    readonly totalCountClicked: () => void
  ) {}
}
