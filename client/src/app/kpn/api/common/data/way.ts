// this class is generated, please do not modify

import {Node} from './node';
import {RawWay} from './raw/raw-way';

export class Way {

  constructor(readonly raw: RawWay,
              readonly nodes: Array<Node>,
              readonly length: number) {
  }

  static fromJSON(jsonObject: any): Way {
    if (!jsonObject) {
      return undefined;
    }
    return new Way(
      RawWay.fromJSON(jsonObject.raw),
      jsonObject.nodes.map((json: any) => Node.fromJSON(json)),
      jsonObject.length
    );
  }
}
