import {List} from 'immutable';
import {RawRelation} from '../common/data/raw/raw-relation';

export class Relation {
  readonly raw: RawRelation;
  readonly members: List<any>;

  constructor(raw: RawRelation,
              members: List<any>) {
    this.raw = raw;
    this.members = members;
  }

  public static fromJSON(jsonObject: any): Relation {
    if (!jsonObject) {
      return undefined;
    }
    return new Relation(
      RawRelation.fromJSON(jsonObject.raw),
      jsonObject.members ? List(jsonObject.members) : List()
    );
  }
}
