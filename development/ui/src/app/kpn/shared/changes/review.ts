// this class is generated, please do not modify

import {Timestamp} from '../timestamp';

export class Review {
  readonly user: string;
  readonly timestamp: Timestamp;
  readonly status: string;
  readonly comment: string;

  constructor(user: string,
              timestamp: Timestamp,
              status: string,
              comment: string) {
    this.user = user;
    this.timestamp = timestamp;
    this.status = status;
    this.comment = comment;
  }

  public static fromJSON(jsonObject): Review {
    if (!jsonObject) {
      return undefined;
    }
    return new Review(
      jsonObject.user,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.status,
      jsonObject.comment
    );
  }
}
