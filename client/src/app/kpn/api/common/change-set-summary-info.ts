// this class is generated, please do not modify

import {ChangeSetSummary} from "./change-set-summary";

export class ChangeSetSummaryInfo {

  constructor(readonly summary: ChangeSetSummary,
              readonly comment: string) {
  }

  public static fromJSON(jsonObject: any): ChangeSetSummaryInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetSummaryInfo(
      ChangeSetSummary.fromJSON(jsonObject.summary),
      jsonObject.comment
    );
  }
}
