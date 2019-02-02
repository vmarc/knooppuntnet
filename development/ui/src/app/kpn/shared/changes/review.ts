// this class is generated, please do not modify

import {Timestamp} from '../timestamp';

export class Review {

  constructor(readonly user: string,
              readonly timestamp: Timestamp,
              readonly status: string,
              readonly comment: string) {
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
