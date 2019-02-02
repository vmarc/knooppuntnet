// this class is generated, please do not modify

import {Relation} from './relation';

export class RelationMember {

  constructor(readonly relation: Relation,
              readonly role: string) {
  }

  public static fromJSON(jsonObject): RelationMember {
    if (!jsonObject) {
      return undefined;
    }
    return new RelationMember(
      Relation.fromJSON(jsonObject.relation),
      jsonObject.role
    );
  }
}
