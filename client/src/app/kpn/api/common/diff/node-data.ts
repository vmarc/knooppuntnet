// this class is generated, please do not modify

import { List } from 'immutable';
import { RawNode } from '../data/raw/raw-node';
import { Subset } from '../../custom/subset';

export class NodeData {
  constructor(
    readonly subsets: List<Subset>,
    readonly name: string,
    readonly node: RawNode
  ) {}

  public static fromJSON(jsonObject: any): NodeData {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeData(
      jsonObject.subsets
        ? List(jsonObject.subsets.map((json: any) => Subset.fromJSON(json)))
        : List(),
      jsonObject.name,
      RawNode.fromJSON(jsonObject.node)
    );
  }
}
