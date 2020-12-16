// this class is generated, please do not modify

import {RawNode} from '../data/raw/raw-node';
import {NodeMoved} from './node/node-moved';
import {TagDiffs} from './tag-diffs';

export class NodeUpdate {

  constructor(readonly before: RawNode,
              readonly after: RawNode,
              readonly tagDiffs: TagDiffs,
              readonly nodeMoved: NodeMoved) {
  }

  public static fromJSON(jsonObject: any): NodeUpdate {
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
