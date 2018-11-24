// this class is generated, please do not modify

import {NodeData} from './node-data';
import {NodeMoved} from './node/node-moved';
import {TagDiffs} from './tag-diffs';

export class NodeDataUpdate {
  readonly before: NodeData;
  readonly after: NodeData;
  readonly tagDiffs: TagDiffs;
  readonly nodeMoved: NodeMoved;

  constructor(before: NodeData,
              after: NodeData,
              tagDiffs: TagDiffs,
              nodeMoved: NodeMoved) {
    this.before = before;
    this.after = after;
    this.tagDiffs = tagDiffs;
    this.nodeMoved = nodeMoved;
  }

  public static fromJSON(jsonObject): NodeDataUpdate {
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
