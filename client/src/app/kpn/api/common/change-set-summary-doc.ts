// this class is generated, please do not modify

import {ChangeSetSummary} from "./change-set-summary";

export class ChangeSetSummaryDoc {

  constructor(readonly _id: string,
              readonly changeSetSummary: ChangeSetSummary,
              readonly _rev: string) {
  }

  public static fromJSON(jsonObject: any): ChangeSetSummaryDoc {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetSummaryDoc(
      jsonObject._id,
      ChangeSetSummary.fromJSON(jsonObject.changeSetSummary),
      jsonObject._rev
    );
  }
}
