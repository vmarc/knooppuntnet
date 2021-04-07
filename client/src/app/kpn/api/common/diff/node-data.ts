// this class is generated, please do not modify

import { RawNode } from '../data/raw/raw-node';
import { Subset } from '../../custom/subset';

export class NodeData {
  constructor(
    readonly subsets: Array<Subset>,
    readonly name: string,
    readonly node: RawNode
  ) {}

  static fromJSON(jsonObject: any): NodeData {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeData(
      jsonObject.subsets.map((json: any) => Subset.fromJSON(json)),
      jsonObject.name,
      RawNode.fromJSON(jsonObject.node)
    );
  }
}
