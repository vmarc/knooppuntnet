// this class is generated, please do not modify

import {ActionTimestamp} from "./action-timestamp";

export class Status {

  constructor(readonly timestamp: ActionTimestamp) {
  }

  public static fromJSON(jsonObject: any): Status {
    if (!jsonObject) {
      return undefined;
    }
    return new Status(
      ActionTimestamp.fromJSON(jsonObject.timestamp)
    );
  }
}
