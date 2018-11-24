// this class is generated, please do not modify

import {List} from 'immutable';
import {MetaData} from '../data/meta-data';
import {NodeUpdate} from './node-update';
import {RawNode} from '../data/raw/raw-node';
import {TagDiffs} from './tag-diffs';

export class WayUpdate {
  readonly id: number;
  readonly before: MetaData;
  readonly after: MetaData;
  readonly removedNodes: List<RawNode>;
  readonly addedNodes: List<RawNode>;
  readonly updatedNodes: List<NodeUpdate>;
  readonly directionReversed: boolean;
  readonly tagDiffs: TagDiffs;

  constructor(id: number,
              before: MetaData,
              after: MetaData,
              removedNodes: List<RawNode>,
              addedNodes: List<RawNode>,
              updatedNodes: List<NodeUpdate>,
              directionReversed: boolean,
              tagDiffs: TagDiffs) {
    this.id = id;
    this.before = before;
    this.after = after;
    this.removedNodes = removedNodes;
    this.addedNodes = addedNodes;
    this.updatedNodes = updatedNodes;
    this.directionReversed = directionReversed;
    this.tagDiffs = tagDiffs;
  }

  public static fromJSON(jsonObject): WayUpdate {
    if (!jsonObject) {
      return undefined;
    }
    return new WayUpdate(
      jsonObject.id,
      MetaData.fromJSON(jsonObject.before),
      MetaData.fromJSON(jsonObject.after),
      jsonObject.removedNodes ? List(jsonObject.removedNodes.map(json => RawNode.fromJSON(json))) : List(),
      jsonObject.addedNodes ? List(jsonObject.addedNodes.map(json => RawNode.fromJSON(json))) : List(),
      jsonObject.updatedNodes ? List(jsonObject.updatedNodes.map(json => NodeUpdate.fromJSON(json))) : List(),
      jsonObject.directionReversed,
      TagDiffs.fromJSON(jsonObject.tagDiffs)
    );
  }
}
