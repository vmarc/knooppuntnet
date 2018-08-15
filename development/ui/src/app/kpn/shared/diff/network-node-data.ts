// this class is generated, please do not modify

import {Country} from '../country';
import {RawNode} from '../data/raw/raw-node';

export class NetworkNodeData {

  constructor(public node?: RawNode,
              public name?: string,
              public country?: Country) {
  }

  public static fromJSON(jsonObject): NetworkNodeData {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkNodeData();
    instance.node = RawNode.fromJSON(jsonObject.node);
    instance.name = jsonObject.name;
    instance.country = Country.fromJSON(jsonObject.country);
    return instance;
  }
}

