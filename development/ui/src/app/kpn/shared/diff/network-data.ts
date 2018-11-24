// this class is generated, please do not modify

import {RawRelation} from '../data/raw/raw-relation';

export class NetworkData {
  readonly relation: RawRelation;
  readonly name: string;

  constructor(relation: RawRelation,
              name: string) {
    this.relation = relation;
    this.name = name;
  }

  public static fromJSON(jsonObject): NetworkData {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkData(
      RawRelation.fromJSON(jsonObject.relation),
      jsonObject.name
    );
  }
}
