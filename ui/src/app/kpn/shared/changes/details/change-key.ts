// this class is generated, please do not modify

import {Timestamp} from '../../timestamp';

export class ChangeKey {

  constructor(public replicationNumber?: number,
              public timestamp?: Timestamp,
              public changeSetId?: number,
              public elementId?: number) {
  }

  public static fromJSON(jsonObject): ChangeKey {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangeKey();
    instance.replicationNumber = jsonObject.replicationNumber;
    instance.timestamp = jsonObject.timestamp;
    instance.changeSetId = jsonObject.changeSetId;
    instance.elementId = jsonObject.elementId;
    return instance;
  }
}

