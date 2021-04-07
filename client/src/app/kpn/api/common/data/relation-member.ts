// this class is generated, please do not modify

import { Relation } from '../../custom/relation';

export class RelationMember {
  constructor(readonly relation: Relation, readonly role: string) {}

  static fromJSON(jsonObject: any): RelationMember {
    if (!jsonObject) {
      return undefined;
    }
    return new RelationMember(
      Relation.fromJSON(jsonObject.relation),
      jsonObject.role
    );
  }
}
