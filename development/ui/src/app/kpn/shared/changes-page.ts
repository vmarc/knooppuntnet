// this class is generated, please do not modify

import {List} from 'immutable';
import {ChangeSetSummaryInfo} from './change-set-summary-info';
import {ChangesFilter} from './changes/filter/changes-filter';

export class ChangesPage {
  readonly filter: ChangesFilter;
  readonly changes: List<ChangeSetSummaryInfo>;
  readonly totalCount: number;

  constructor(filter: ChangesFilter,
              changes: List<ChangeSetSummaryInfo>,
              totalCount: number) {
    this.filter = filter;
    this.changes = changes;
    this.totalCount = totalCount;
  }

  public static fromJSON(jsonObject): ChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangesPage(
      ChangesFilter.fromJSON(jsonObject.filter),
      jsonObject.changes ? List(jsonObject.changes.map(json => ChangeSetSummaryInfo.fromJSON(json))) : List(),
      jsonObject.totalCount
    );
  }
}
