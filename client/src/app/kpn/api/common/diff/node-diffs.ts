// this class is generated, please do not modify

import { List } from 'immutable';
import { NodeData } from './node-data';
import { NodeDataUpdate } from './node-data-update';

export class NodeDiffs {
  constructor(
    readonly removed: List<NodeData>,
    readonly added: List<NodeData>,
    readonly updated: List<NodeDataUpdate>
  ) {}

  public static fromJSON(jsonObject: any): NodeDiffs {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeDiffs(
      jsonObject.removed
        ? List(jsonObject.removed.map((json: any) => NodeData.fromJSON(json)))
        : List(),
      jsonObject.added
        ? List(jsonObject.added.map((json: any) => NodeData.fromJSON(json)))
        : List(),
      jsonObject.updated
        ? List(
            jsonObject.updated.map((json: any) => NodeDataUpdate.fromJSON(json))
          )
        : List()
    );
  }
}
