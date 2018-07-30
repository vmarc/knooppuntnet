// this class is generated, please do not modify

import {Timestamp} from '../timestamp';

export class MetaData {

  constructor(public version?: number,
              public timestamp?: Timestamp,
              public changeSetId?: number) {
  }

  public static fromJSON(jsonObject): MetaData {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new MetaData();
    instance.version = jsonObject.version;
    instance.timestamp = jsonObject.timestamp;
    instance.changeSetId = jsonObject.changeSetId;
    return instance;
  }
}

