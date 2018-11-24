// this class is generated, please do not modify

import {Country} from '../country';
import {RawNode} from '../data/raw/raw-node';

export class NetworkNodeData {
  readonly node: RawNode;
  readonly name: string;
  readonly country: Country;

  constructor(node: RawNode,
              name: string,
              country: Country) {
    this.node = node;
    this.name = name;
    this.country = country;
  }

  public static fromJSON(jsonObject): NetworkNodeData {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkNodeData(
      RawNode.fromJSON(jsonObject.node),
      jsonObject.name,
      Country.fromJSON(jsonObject.country)
    );
  }
}
