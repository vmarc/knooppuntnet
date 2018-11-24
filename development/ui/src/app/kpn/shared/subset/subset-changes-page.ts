// this class is generated, please do not modify

import {List} from 'immutable';
import {ChangeSetSummaryInfo} from '../change-set-summary-info';
import {ChangesFilter} from '../changes/filter/changes-filter';
import {SubsetInfo} from './subset-info';

export class SubsetChangesPage {
  readonly subsetInfo: SubsetInfo;
  readonly filter: ChangesFilter;
  readonly changes: List<ChangeSetSummaryInfo>;
  readonly totalCount: number;

  constructor(subsetInfo: SubsetInfo,
              filter: ChangesFilter,
              changes: List<ChangeSetSummaryInfo>,
              totalCount: number) {
    this.subsetInfo = subsetInfo;
    this.filter = filter;
    this.changes = changes;
    this.totalCount = totalCount;
  }

  public static fromJSON(jsonObject): SubsetChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetChangesPage(
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      ChangesFilter.fromJSON(jsonObject.filter),
      jsonObject.changes ? List(jsonObject.changes.map(json => ChangeSetSummaryInfo.fromJSON(json))) : List(),
      jsonObject.totalCount
    );
  }
}
