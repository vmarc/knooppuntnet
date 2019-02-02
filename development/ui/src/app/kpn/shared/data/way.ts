// this class is generated, please do not modify

import {List} from 'immutable';
import {Node} from './node';
import {RawWay} from './raw/raw-way';

export class Way {

  constructor(readonly raw: RawWay,
              readonly nodes: List<Node>,
              readonly length: number) {
  }

  public static fromJSON(jsonObject): Way {
    if (!jsonObject) {
      return undefined;
    }
    return new Way(
      RawWay.fromJSON(jsonObject.raw),
      jsonObject.nodes ? List(jsonObject.nodes.map(json => Node.fromJSON(json))) : List(),
      jsonObject.length
    );
  }
}
