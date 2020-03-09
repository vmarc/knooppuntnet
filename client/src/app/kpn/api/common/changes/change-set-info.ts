// this class is generated, please do not modify

import {Tags} from "../../custom/tags";
import {Timestamp} from "../../custom/timestamp";

export class ChangeSetInfo {

  constructor(readonly id: number,
              readonly createdAt: Timestamp,
              readonly closedAt: Timestamp,
              readonly open: boolean,
              readonly commentsCount: number,
              readonly tags: Tags) {
  }

  public static fromJSON(jsonObject: any): ChangeSetInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetInfo(
      jsonObject.id,
      Timestamp.fromJSON(jsonObject.createdAt),
      Timestamp.fromJSON(jsonObject.closedAt),
      jsonObject.open,
      jsonObject.commentsCount,
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
