// this class is generated, please do not modify

import { NodeData } from './node-data';
import { NodeMoved } from './node/node-moved';
import { TagDiffs } from './tag-diffs';

export class NodeDataUpdate {
  constructor(
    readonly before: NodeData,
    readonly after: NodeData,
    readonly tagDiffs: TagDiffs,
    readonly nodeMoved: NodeMoved
  ) {}

  public static fromJSON(jsonObject: any): NodeDataUpdate {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeDataUpdate(
      NodeData.fromJSON(jsonObject.before),
      NodeData.fromJSON(jsonObject.after),
      TagDiffs.fromJSON(jsonObject.tagDiffs),
      NodeMoved.fromJSON(jsonObject.nodeMoved)
    );
  }
}
