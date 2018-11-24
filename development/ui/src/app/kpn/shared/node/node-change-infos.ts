// this class is generated, please do not modify

import {List} from 'immutable';
import {NodeChangeInfo} from './node-change-info';

export class NodeChangeInfos {
  readonly changes: List<NodeChangeInfo>;
  readonly incompleteWarning: boolean;
  readonly more: boolean;

  constructor(changes: List<NodeChangeInfo>,
              incompleteWarning: boolean,
              more: boolean) {
    this.changes = changes;
    this.incompleteWarning = incompleteWarning;
    this.more = more;
  }

  public static fromJSON(jsonObject): NodeChangeInfos {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeChangeInfos(
      jsonObject.changes ? List(jsonObject.changes.map(json => NodeChangeInfo.fromJSON(json))) : List(),
      jsonObject.incompleteWarning,
      jsonObject.more
    );
  }
}
