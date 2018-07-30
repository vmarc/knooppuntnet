// this class is generated, please do not modify

import {ChangeSetSummaryInfo} from '../change-set-summary-info';
import {ChangesFilter} from '../changes/filter/changes-filter';
import {SubsetInfo} from './subset-info';

export class SubsetChangesPage {

  constructor(public subsetInfo?: SubsetInfo,
              public filter?: ChangesFilter,
              public changes?: Array<ChangeSetSummaryInfo>,
              public totalCount?: number) {
  }

  public static fromJSON(jsonObject): SubsetChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new SubsetChangesPage();
    instance.subsetInfo = SubsetInfo.fromJSON(jsonObject.subsetInfo);
    instance.filter = ChangesFilter.fromJSON(jsonObject.filter);
    instance.changes = jsonObject.changes ? jsonObject.changes.map(json => ChangeSetSummaryInfo.fromJSON(json)) : [];
    instance.totalCount = jsonObject.totalCount;
    return instance;
  }
}

