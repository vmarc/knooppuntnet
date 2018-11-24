// this class is generated, please do not modify

import {ChangeSetSummary} from './change-set-summary';

export class ChangeSetSummaryDoc {
  readonly _id: string;
  readonly changeSetSummary: ChangeSetSummary;
  readonly _rev: string;

  constructor(_id: string,
              changeSetSummary: ChangeSetSummary,
              _rev: string) {
    this._id = _id;
    this.changeSetSummary = changeSetSummary;
    this._rev = _rev;
  }

  public static fromJSON(jsonObject): ChangeSetSummaryDoc {
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
