// this class is generated, please do not modify

import {List} from 'immutable';
import {RawNode} from '../data/raw/raw-node';
import {RawWay} from '../data/raw/raw-way';

export class WayData {
  readonly way: RawWay;
  readonly nodes: List<RawNode>;

  constructor(way: RawWay,
              nodes: List<RawNode>) {
    this.way = way;
    this.nodes = nodes;
  }

  public static fromJSON(jsonObject): WayData {
    if (!jsonObject) {
      return undefined;
    }
    return new WayData(
      RawWay.fromJSON(jsonObject.way),
      jsonObject.nodes ? List(jsonObject.nodes.map(json => RawNode.fromJSON(json))) : List()
    );
  }
}
