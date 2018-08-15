// this class is generated, please do not modify

import {RawRelation} from '../data/raw/raw-relation';

export class NetworkData {

  constructor(public relation?: RawRelation,
              public name?: string) {
  }

  public static fromJSON(jsonObject): NetworkData {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkData();
    instance.relation = RawRelation.fromJSON(jsonObject.relation);
    instance.name = jsonObject.name;
    return instance;
  }
}

