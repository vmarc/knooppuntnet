// this class is generated, please do not modify

import {NodeData} from './node-data';
import {NodeDataUpdate} from './node-data-update';

export class NodeDiffs {

  constructor(public removed?: Array<NodeData>,
              public added?: Array<NodeData>,
              public updated?: Array<NodeDataUpdate>) {
  }

  public static fromJSON(jsonObject): NodeDiffs {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeDiffs();
    instance.removed = jsonObject.removed ? jsonObject.removed.map(json => NodeData.fromJSON(json)) : [];
    instance.added = jsonObject.added ? jsonObject.added.map(json => NodeData.fromJSON(json)) : [];
    instance.updated = jsonObject.updated ? jsonObject.updated.map(json => NodeDataUpdate.fromJSON(json)) : [];
    return instance;
  }
}

