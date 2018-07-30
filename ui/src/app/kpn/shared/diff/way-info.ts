// this class is generated, please do not modify

import {Tags} from '../data/tags';
import {Timestamp} from '../timestamp';

export class WayInfo {

  constructor(public id?: number,
              public version?: number,
              public changeSetId?: number,
              public timestamp?: Timestamp,
              public tags?: Tags) {
  }

  public static fromJSON(jsonObject): WayInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new WayInfo();
    instance.id = jsonObject.id;
    instance.version = jsonObject.version;
    instance.changeSetId = jsonObject.changeSetId;
    instance.timestamp = jsonObject.timestamp;
    instance.tags = jsonObject.tags;
    return instance;
  }
}

