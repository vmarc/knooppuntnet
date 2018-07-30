// this class is generated, please do not modify

import {Tags} from '../tags';
import {Timestamp} from '../../timestamp';

export class RawNode {

  constructor(public id?: number,
              public latitude?: string,
              public longitude?: string,
              public version?: number,
              public timestamp?: Timestamp,
              public changeSetId?: number,
              public tags?: Tags) {
  }

  public static fromJSON(jsonObject): RawNode {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RawNode();
    instance.id = jsonObject.id;
    instance.latitude = jsonObject.latitude;
    instance.longitude = jsonObject.longitude;
    instance.version = jsonObject.version;
    instance.timestamp = jsonObject.timestamp;
    instance.changeSetId = jsonObject.changeSetId;
    instance.tags = jsonObject.tags;
    return instance;
  }
}

