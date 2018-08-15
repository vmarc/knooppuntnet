// this class is generated, please do not modify

import {Node} from './node';
import {RawWay} from './raw/raw-way';

export class Way {

  constructor(public raw?: RawWay,
              public nodes?: Array<Node>,
              public length?: number) {
  }

  public static fromJSON(jsonObject): Way {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Way();
    instance.raw = RawWay.fromJSON(jsonObject.raw);
    instance.nodes = jsonObject.nodes ? jsonObject.nodes.map(json => Node.fromJSON(json)) : [];
    instance.length = jsonObject.length;
    return instance;
  }
}

