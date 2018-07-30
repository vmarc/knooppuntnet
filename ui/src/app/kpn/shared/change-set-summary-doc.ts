// this class is generated, please do not modify

import {ChangeSetSummary} from './change-set-summary';

export class ChangeSetSummaryDoc {

  constructor(public _id?: string,
              public changeSetSummary?: ChangeSetSummary,
              public _rev?: string) {
  }

  public static fromJSON(jsonObject): ChangeSetSummaryDoc {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangeSetSummaryDoc();
    instance._id = jsonObject._id;
    instance.changeSetSummary = ChangeSetSummary.fromJSON(jsonObject.changeSetSummary);
    instance._rev = jsonObject._rev;
    return instance;
  }
}

