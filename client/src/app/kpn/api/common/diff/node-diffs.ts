// this class is generated, please do not modify

import {NodeData} from './node-data';
import {NodeDataUpdate} from './node-data-update';

export class NodeDiffs {

  constructor(readonly removed: Array<NodeData>,
              readonly added: Array<NodeData>,
              readonly updated: Array<NodeDataUpdate>) {
  }

  public static fromJSON(jsonObject: any): NodeDiffs {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeDiffs(
      jsonObject.removed.map((json: any) => NodeData.fromJSON(json)),
      jsonObject.added.map((json: any) => NodeData.fromJSON(json)),
      jsonObject.updated.map((json: any) => NodeDataUpdate.fromJSON(json))
    );
  }
}
