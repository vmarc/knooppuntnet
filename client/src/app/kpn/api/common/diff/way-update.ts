// this class is generated, please do not modify

import { MetaData } from '../data/meta-data';
import { NodeUpdate } from './node-update';
import { RawNode } from '../data/raw/raw-node';
import { TagDiffs } from './tag-diffs';

export class WayUpdate {
  constructor(
    readonly id: number,
    readonly before: MetaData,
    readonly after: MetaData,
    readonly removedNodes: Array<RawNode>,
    readonly addedNodes: Array<RawNode>,
    readonly updatedNodes: Array<NodeUpdate>,
    readonly directionReversed: boolean,
    readonly tagDiffs: TagDiffs
  ) {}

  static fromJSON(jsonObject: any): WayUpdate {
    if (!jsonObject) {
      return undefined;
    }
    return new WayUpdate(
      jsonObject.id,
      MetaData.fromJSON(jsonObject.before),
      MetaData.fromJSON(jsonObject.after),
      jsonObject.removedNodes.map((json: any) => RawNode.fromJSON(json)),
      jsonObject.addedNodes.map((json: any) => RawNode.fromJSON(json)),
      jsonObject.updatedNodes.map((json: any) => NodeUpdate.fromJSON(json)),
      jsonObject.directionReversed,
      TagDiffs.fromJSON(jsonObject.tagDiffs)
    );
  }
}
