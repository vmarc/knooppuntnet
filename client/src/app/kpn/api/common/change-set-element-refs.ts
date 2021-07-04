// this class is generated, please do not modify

import { List } from 'immutable';
import { ChangeSetElementRef } from './change-set-element-ref';

export class ChangeSetElementRefs {
  constructor(
    readonly removed: List<ChangeSetElementRef>,
    readonly added: List<ChangeSetElementRef>,
    readonly updated: List<ChangeSetElementRef>
  ) {}

  public static fromJSON(jsonObject: any): ChangeSetElementRefs {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetElementRefs(
      jsonObject.removed
        ? List(
            jsonObject.removed.map((json: any) =>
              ChangeSetElementRef.fromJSON(json)
            )
          )
        : List(),
      jsonObject.added
        ? List(
            jsonObject.added.map((json: any) =>
              ChangeSetElementRef.fromJSON(json)
            )
          )
        : List(),
      jsonObject.updated
        ? List(
            jsonObject.updated.map((json: any) =>
              ChangeSetElementRef.fromJSON(json)
            )
          )
        : List()
    );
  }
}
