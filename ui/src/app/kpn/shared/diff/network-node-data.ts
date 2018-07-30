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
    instance.node = jsonObject.node;
    instance.name = jsonObject.name;
    instance.country = jsonObject.country;
    return instance;
  }
}

