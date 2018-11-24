// this class is generated, please do not modify

import {NodeInfo} from '../node-info';
import {NodeReferences} from './node-references';

export class MapDetailNode {
  readonly info: NodeInfo;
  readonly references: NodeReferences;

  constructor(info: NodeInfo,
              references: NodeReferences) {
    this.info = info;
    this.references = references;
  }

  public static fromJSON(jsonObject): MapDetailNode {
    if (!jsonObject) {
      return undefined;
    }
    return new MapDetailNode(
      NodeInfo.fromJSON(jsonObject.info),
      NodeReferences.fromJSON(jsonObject.references)
    );
  }
}
