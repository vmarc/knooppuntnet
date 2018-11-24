// this class is generated, please do not modify

import {List} from 'immutable';
import {RawNode} from '../data/raw/raw-node';
import {Subset} from '../subset';

export class NodeData {
  readonly subsets: List<Subset>;
  readonly name: string;
  readonly node: RawNode;

  constructor(subsets: List<Subset>,
              name: string,
              node: RawNode) {
    this.subsets = subsets;
    this.name = name;
    this.node = node;
  }

  public static fromJSON(jsonObject): NodeData {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeData(
      jsonObject.subsets ? List(jsonObject.subsets.map(json => Subset.fromJSON(json))) : List(),
      jsonObject.name,
      RawNode.fromJSON(jsonObject.node)
    );
  }
}
