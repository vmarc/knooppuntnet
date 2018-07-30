// this class is generated, please do not modify

import {Country} from '../country';
import {Fact} from '../fact';
import {NetworkType} from '../network-type';
import {RawNode} from '../data/raw/raw-node';
import {RawRelation} from '../data/raw/raw-relation';
import {RawWay} from '../data/raw/raw-way';

export class RouteData {

  constructor(public country?: Country,
              public networkType?: NetworkType,
              public relation?: RawRelation,
              public name?: string,
              public networkNodes?: Array<RawNode>,
              public nodes?: Array<RawNode>,
              public ways?: Array<RawWay>,
              public relations?: Array<RawRelation>,
              public facts?: Array<Fact>) {
  }

  public static fromJSON(jsonObject): RouteData {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteData();
    instance.country = Country.fromJSON(jsonObject.country);
    instance.networkType = NetworkType.fromJSON(jsonObject.networkType);
    instance.relation = RawRelation.fromJSON(jsonObject.relation);
    instance.name = jsonObject.name;
    instance.networkNodes = jsonObject.networkNodes ? jsonObject.networkNodes.map(json => RawNode.fromJSON(json)) : [];
    instance.nodes = jsonObject.nodes ? jsonObject.nodes.map(json => RawNode.fromJSON(json)) : [];
    instance.ways = jsonObject.ways ? jsonObject.ways.map(json => RawWay.fromJSON(json)) : [];
    instance.relations = jsonObject.relations ? jsonObject.relations.map(json => RawRelation.fromJSON(json)) : [];
    instance.facts = jsonObject.facts ? jsonObject.facts.map(json => Fact.fromJSON(json)) : [];
    return instance;
  }
}

