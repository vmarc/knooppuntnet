// this class is generated, please do not modify

import {ChangeSetSummaryInfo} from './change-set-summary-info';
import {ChangesFilter} from './changes/filter/changes-filter';

export class ChangesPage {

  constructor(public filter?: ChangesFilter,
              public changes?: Array<ChangeSetSummaryInfo>,
              public totalCount?: number) {
  }

  public static fromJSON(jsonObject): ChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangesPage();
    instance.filter = ChangesFilter.fromJSON(jsonObject.filter);
    instance.changes = jsonObject.changes ? jsonObject.changes.map(json => ChangeSetSummaryInfo.fromJSON(json)) : [];
    instance.totalCount = jsonObject.totalCount;
    return instance;
  }
}

