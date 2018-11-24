// this class is generated, please do not modify

import {Relation} from './relation';

export class RelationMember {
  readonly relation: Relation;
  readonly role: string;

  constructor(relation: Relation,
              role: string) {
    this.relation = relation;
    this.role = role;
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
