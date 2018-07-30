// this class is generated, please do not modify

import {MetaData} from '../data/meta-data';
import {NodeUpdate} from './node-update';
import {RawNode} from '../data/raw/raw-node';
import {TagDiffs} from './tag-diffs';

export class WayUpdate {

  constructor(public id?: number,
              public before?: MetaData,
              public after?: MetaData,
              public removedNodes?: Array<RawNode>,
              public addedNodes?: Array<RawNode>,
              public updatedNodes?: Array<NodeUpdate>,
              public directionReversed?: boolean,
              public tagDiffs?: TagDiffs) {
  }

  public static fromJSON(jsonObject): WayUpdate {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new WayUpdate();
    instance.id = jsonObject.id;
    instance.before = jsonObject.before;
    instance.after = jsonObject.after;
    instance.removedNodes = jsonObject.removedNodes ? jsonObject.removedNodes.map(json => RawNode.fromJSON(json)) : [];
    instance.addedNodes = jsonObject.addedNodes ? jsonObject.addedNodes.map(json => RawNode.fromJSON(json)) : [];
    instance.updatedNodes = jsonObject.updatedNodes ? jsonObject.updatedNodes.map(json => NodeUpdate.fromJSON(json)) : [];
    instance.directionReversed = jsonObject.directionReversed;
    instance.tagDiffs = jsonObject.tagDiffs;
    return instance;
  }
}

