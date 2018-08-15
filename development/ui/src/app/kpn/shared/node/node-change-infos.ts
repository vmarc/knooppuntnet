// this class is generated, please do not modify

import {NodeChangeInfo} from './node-change-info';

export class NodeChangeInfos {

  constructor(public changes?: Array<NodeChangeInfo>,
              public incompleteWarning?: boolean,
              public more?: boolean) {
  }

  public static fromJSON(jsonObject): NodeChangeInfos {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeChangeInfos();
    instance.changes = jsonObject.changes ? jsonObject.changes.map(json => NodeChangeInfo.fromJSON(json)) : [];
    instance.incompleteWarning = jsonObject.incompleteWarning;
    instance.more = jsonObject.more;
    return instance;
  }
}

