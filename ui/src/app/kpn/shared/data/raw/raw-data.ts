// this class is generated, please do not modify

import {RawNode} from './raw-node';
import {RawRelation} from './raw-relation';
import {RawWay} from './raw-way';
import {Timestamp} from '../../timestamp';

export class RawData {

  constructor(public timestamp?: Timestamp,
              public nodes?: Array<RawNode>,
              public ways?: Array<RawWay>,
              public relations?: Array<RawRelation>) {
  }

  public static fromJSON(jsonObject): RawData {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RawData();
    instance.timestamp = Timestamp.fromJSON(jsonObject.timestamp);
    instance.nodes = jsonObject.nodes ? jsonObject.nodes.map(json => RawNode.fromJSON(json)) : [];
    instance.ways = jsonObject.ways ? jsonObject.ways.map(json => RawWay.fromJSON(json)) : [];
    instance.relations = jsonObject.relations ? jsonObject.relations.map(json => RawRelation.fromJSON(json)) : [];
    return instance;
  }
}

