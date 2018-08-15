// this class is generated, please do not modify

import {Timestamp} from '../timestamp';

export class Review {

  constructor(public user?: string,
              public timestamp?: Timestamp,
              public status?: string,
              public comment?: string) {
  }

  public static fromJSON(jsonObject): Review {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Review();
    instance.user = jsonObject.user;
    instance.timestamp = Timestamp.fromJSON(jsonObject.timestamp);
    instance.status = jsonObject.status;
    instance.comment = jsonObject.comment;
    return instance;
  }
}

