// this class is generated, please do not modify

import {RawNode} from '../data/raw/raw-node';
import {Subset} from '../subset';

export class NodeData {

  constructor(public subsets?: Array<Subset>,
              public name?: string,
              public node?: RawNode) {
  }

  public static fromJSON(jsonObject): NodeData {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeData();
    instance.subsets = jsonObject.subsets ? jsonObject.subsets.map(json => Subset.fromJSON(json)) : [];
    instance.name = jsonObject.name;
    instance.node = RawNode.fromJSON(jsonObject.node);
    return instance;
  }
}

