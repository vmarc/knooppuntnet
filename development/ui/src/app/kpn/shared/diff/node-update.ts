// this class is generated, please do not modify

import {NodeMoved} from './node/node-moved';
import {RawNode} from '../data/raw/raw-node';
import {TagDiffs} from './tag-diffs';

export class NodeUpdate {
  readonly before: RawNode;
  readonly after: RawNode;
  readonly tagDiffs: TagDiffs;
  readonly nodeMoved: NodeMoved;

  constructor(before: RawNode,
              after: RawNode,
              tagDiffs: TagDiffs,
              nodeMoved: NodeMoved) {
    this.before = before;
    this.after = after;
    this.tagDiffs = tagDiffs;
    this.nodeMoved = nodeMoved;
  }

  public static fromJSON(jsonObject): NodeUpdate {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeUpdate(
      RawNode.fromJSON(jsonObject.before),
      RawNode.fromJSON(jsonObject.after),
      TagDiffs.fromJSON(jsonObject.tagDiffs),
      NodeMoved.fromJSON(jsonObject.nodeMoved)
    );
  }
}
