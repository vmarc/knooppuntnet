// this class is generated, please do not modify

import { Timestamp } from '../../../custom/timestamp';
import { RawNode } from './raw-node';
import { RawRelation } from './raw-relation';
import { RawWay } from './raw-way';

export class RawData {
  constructor(
    readonly timestamp: Timestamp,
    readonly nodes: Array<RawNode>,
    readonly ways: Array<RawWay>,
    readonly relations: Array<RawRelation>
  ) {}

  static fromJSON(jsonObject: any): RawData {
    if (!jsonObject) {
      return undefined;
    }
    return new RawData(
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.nodes.map((json: any) => RawNode.fromJSON(json)),
      jsonObject.ways.map((json: any) => RawWay.fromJSON(json)),
      jsonObject.relations.map((json: any) => RawRelation.fromJSON(json))
    );
  }
}
