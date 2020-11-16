// this class is generated, please do not modify

import {List} from 'immutable';
import {NodeChangeInfo} from './node-change-info';

export class NodeChangeInfos {

  constructor(readonly changes: List<NodeChangeInfo>,
              readonly incompleteWarning: boolean) {
  }

  public static fromJSON(jsonObject: any): NodeChangeInfos {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeChangeInfos(
      jsonObject.changes ? List(jsonObject.changes.map((json: any) => NodeChangeInfo.fromJSON(json))) : List(),
      jsonObject.incompleteWarning
    );
  }
}
