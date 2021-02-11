// this class is generated, please do not modify

import {NodeChangeInfo} from './node-change-info';

export class NodeChangeInfos {

  constructor(readonly changes: Array<NodeChangeInfo>,
              readonly incompleteWarning: boolean) {
  }

  public static fromJSON(jsonObject: any): NodeChangeInfos {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeChangeInfos(
      jsonObject.changes.map((json: any) => NodeChangeInfo.fromJSON(json)),
      jsonObject.incompleteWarning
    );
  }
}
