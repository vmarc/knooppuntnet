// this class is generated, please do not modify

import {Tags} from '../tags';
import {Timestamp} from '../../timestamp';

export class RawWay {

  constructor(public id?: number,
              public version?: number,
              public timestamp?: Timestamp,
              public changeSetId?: number,
              public nodeIds?: Array<number>,
              public tags?: Tags) {
  }

  public static fromJSON(jsonObject): RawWay {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RawWay();
    instance.id = jsonObject.id;
    instance.version = jsonObject.version;
    instance.timestamp = jsonObject.timestamp;
    instance.changeSetId = jsonObject.changeSetId;
    instance.nodeIds = jsonObject.nodeIds;
    instance.tags = jsonObject.tags;
    return instance;
  }
}

