// this class is generated, please do not modify

import { RawRelation } from '../data/raw/raw-relation';

export class NetworkData {
  constructor(readonly relation: RawRelation, readonly name: string) {}

  public static fromJSON(jsonObject: any): NetworkData {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkData(
      RawRelation.fromJSON(jsonObject.relation),
      jsonObject.name
    );
  }
}
