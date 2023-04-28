import { ChangesFilterPeriod } from '@api/common/changes/filter';
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
