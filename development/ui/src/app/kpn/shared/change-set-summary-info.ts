// this class is generated, please do not modify

import {ChangeSetSummary} from './change-set-summary';

export class ChangeSetSummaryInfo {

  constructor(public summary?: ChangeSetSummary,
              public comment?: string) {
  }

  public static fromJSON(jsonObject): ChangeSetSummaryInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangeSetSummaryInfo();
    instance.summary = ChangeSetSummary.fromJSON(jsonObject.summary);
    instance.comment = jsonObject.comment;
    return instance;
  }
}

