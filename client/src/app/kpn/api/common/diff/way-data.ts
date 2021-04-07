// this class is generated, please do not modify

import { RawNode } from '../data/raw/raw-node';
import { RawWay } from '../data/raw/raw-way';

export class WayData {
  constructor(readonly way: RawWay, readonly nodes: Array<RawNode>) {}

  public static fromJSON(jsonObject: any): WayData {
    if (!jsonObject) {
      return undefined;
    }
    return new WayData(
      RawWay.fromJSON(jsonObject.way),
      jsonObject.nodes.map((json: any) => RawNode.fromJSON(json))
    );
  }
}
