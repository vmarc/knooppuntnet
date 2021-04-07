// this class is generated, please do not modify

import { ChangeSetSummaryInfo } from './change-set-summary-info';
import { ChangesFilter } from './changes/filter/changes-filter';

export class ChangesPage {
  constructor(
    readonly filter: ChangesFilter,
    readonly changes: Array<ChangeSetSummaryInfo>,
    readonly changeCount: number
  ) {}

  static fromJSON(jsonObject: any): ChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangesPage(
      ChangesFilter.fromJSON(jsonObject.filter),
      jsonObject.changes?.map((json: any) =>
        ChangeSetSummaryInfo.fromJSON(json)
      ),
      jsonObject.changeCount
    );
  }
}
