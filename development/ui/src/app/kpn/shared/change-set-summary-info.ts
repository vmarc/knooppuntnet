// this class is generated, please do not modify

import {ChangeSetSummary} from './change-set-summary';

export class ChangeSetSummaryInfo {
  readonly summary: ChangeSetSummary;
  readonly comment: string;

  constructor(summary: ChangeSetSummary,
              comment: string) {
    this.summary = summary;
    this.comment = comment;
  }

  public static fromJSON(jsonObject): ChangeSetSummaryInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetSummaryInfo(
      ChangeSetSummary.fromJSON(jsonObject.summary),
      jsonObject.comment
    );
  }
}
