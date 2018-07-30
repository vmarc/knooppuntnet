// this class is generated, please do not modify

import {RawNode} from '../data/raw/raw-node';
import {RawWay} from '../data/raw/raw-way';

export class WayData {

  constructor(public way?: RawWay,
              public nodes?: Array<RawNode>) {
  }

  public static fromJSON(jsonObject): WayData {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new WayData();
    instance.way = RawWay.fromJSON(jsonObject.way);
    instance.nodes = jsonObject.nodes ? jsonObject.nodes.map(json => RawNode.fromJSON(json)) : [];
    return instance;
  }
}

