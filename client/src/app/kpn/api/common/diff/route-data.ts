// this class is generated, please do not modify

import {Country} from '../../custom/country';
import {Fact} from '../../custom/fact';
import {NetworkType} from '../../custom/network-type';
import {RawNode} from '../data/raw/raw-node';
import {RawRelation} from '../data/raw/raw-relation';
import {RawWay} from '../data/raw/raw-way';

export class RouteData {

  constructor(readonly country: Country,
              readonly networkType: NetworkType,
              readonly relation: RawRelation,
              readonly name: string,
              readonly networkNodes: Array<RawNode>,
              readonly nodes: Array<RawNode>,
              readonly ways: Array<RawWay>,
              readonly relations: Array<RawRelation>,
              readonly facts: Array<Fact>) {
  }

  public static fromJSON(jsonObject: any): RouteData {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteData(
      jsonObject.country,
      jsonObject.networkType,
      RawRelation.fromJSON(jsonObject.relation),
      jsonObject.name,
      jsonObject.networkNodes.map((json: any) => RawNode.fromJSON(json)),
      jsonObject.nodes.map((json: any) => RawNode.fromJSON(json)),
      jsonObject.ways.map((json: any) => RawWay.fromJSON(json)),
      jsonObject.relations.map((json: any) => RawRelation.fromJSON(json)),
      jsonObject.facts
    );
  }
}
