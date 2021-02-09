// this class is generated, please do not modify

import {ChangeSetElementRef} from './change-set-element-ref';

export class ChangeSetElementRefs {

  constructor(readonly removed: Array<ChangeSetElementRef>,
              readonly added: Array<ChangeSetElementRef>,
              readonly updated: Array<ChangeSetElementRef>) {
  }

  public static fromJSON(jsonObject: any): ChangeSetElementRefs {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetElementRefs(
      jsonObject.removed ? Array(jsonObject.removed.map((json: any) => ChangeSetElementRef.fromJSON(json))) : Array(),
      jsonObject.added ? Array(jsonObject.added.map((json: any) => ChangeSetElementRef.fromJSON(json))) : Array(),
      jsonObject.updated ? Array(jsonObject.updated.map((json: any) => ChangeSetElementRef.fromJSON(json))) : Array()
    );
  }
}
