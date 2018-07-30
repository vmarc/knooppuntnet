// this class is generated, please do not modify

import {NodeData} from './node-data';
import {NodeMoved} from './node/node-moved';
import {TagDiffs} from './tag-diffs';

export class NodeDataUpdate {

  constructor(public before?: NodeData,
              public after?: NodeData,
              public tagDiffs?: TagDiffs,
              public nodeMoved?: NodeMoved) {
  }

  public static fromJSON(jsonObject): NodeDataUpdate {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeDataUpdate();
    instance.before = NodeData.fromJSON(jsonObject.before);
    instance.after = NodeData.fromJSON(jsonObject.after);
    instance.tagDiffs = TagDiffs.fromJSON(jsonObject.tagDiffs);
    instance.nodeMoved = NodeMoved.fromJSON(jsonObject.nodeMoved);
    return instance;
  }
}

