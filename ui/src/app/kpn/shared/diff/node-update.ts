// this class is generated, please do not modify

import {NodeMoved} from './node/node-moved';
import {RawNode} from '../data/raw/raw-node';
import {TagDiffs} from './tag-diffs';

export class NodeUpdate {

  constructor(public before?: RawNode,
              public after?: RawNode,
              public tagDiffs?: TagDiffs,
              public nodeMoved?: NodeMoved) {
  }

  public static fromJSON(jsonObject): NodeUpdate {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeUpdate();
    instance.before = jsonObject.before;
    instance.after = jsonObject.after;
    instance.tagDiffs = jsonObject.tagDiffs;
    instance.nodeMoved = jsonObject.nodeMoved;
    return instance;
  }
}

