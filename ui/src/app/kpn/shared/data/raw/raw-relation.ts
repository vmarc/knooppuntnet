// this class is generated, please do not modify

import {RawMember} from './raw-member';
import {Tags} from '../tags';
import {Timestamp} from '../../timestamp';

export class RawRelation {

  constructor(public id?: number,
              public version?: number,
              public timestamp?: Timestamp,
              public changeSetId?: number,
              public members?: Array<RawMember>,
              public tags?: Tags) {
  }

  public static fromJSON(jsonObject): RawRelation {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RawRelation();
    instance.id = jsonObject.id;
    instance.version = jsonObject.version;
    instance.timestamp = jsonObject.timestamp;
    instance.changeSetId = jsonObject.changeSetId;
    instance.members = jsonObject.members ? jsonObject.members.map(json => RawMember.fromJSON(json)) : [];
    instance.tags = jsonObject.tags;
    return instance;
  }
}

