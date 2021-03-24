// this class is generated, please do not modify

import {ChangeSetElementRef} from './change-set-element-ref';

export class ChangeSetElementRefs {

  constructor(readonly removed: Array<ChangeSetElementRef>,
              readonly added: Array<ChangeSetElementRef>,
              readonly updated: Array<ChangeSetElementRef>) {
  }

  static fromJSON(jsonObject: any): ChangeSetElementRefs {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetElementRefs(
      jsonObject.removed?.map((json: any) => ChangeSetElementRef.fromJSON(json)),
      jsonObject.added?.map((json: any) => ChangeSetElementRef.fromJSON(json)),
      jsonObject.updated?.map((json: any) => ChangeSetElementRef.fromJSON(json))
    );
  }
}
