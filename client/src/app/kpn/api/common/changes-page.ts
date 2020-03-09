// this class is generated, please do not modify

import {List} from "immutable";
import {ChangeSetSummaryInfo} from "./change-set-summary-info";
import {ChangesFilter} from "./changes/filter/changes-filter";

export class ChangesPage {

  constructor(readonly filter: ChangesFilter,
              readonly changes: List<ChangeSetSummaryInfo>,
              readonly changeCount: number) {
  }

  public static fromJSON(jsonObject: any): ChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangesPage(
      ChangesFilter.fromJSON(jsonObject.filter),
      jsonObject.changes ? List(jsonObject.changes.map(json => ChangeSetSummaryInfo.fromJSON(json))) : List(),
      jsonObject.changeCount
    );
  }
}
