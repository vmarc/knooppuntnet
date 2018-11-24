// this class is generated, please do not modify

import {List} from 'immutable';
import {Country} from '../country';
import {Fact} from '../fact';
import {NetworkType} from '../network-type';
import {RawNode} from '../data/raw/raw-node';
import {RawRelation} from '../data/raw/raw-relation';
import {RawWay} from '../data/raw/raw-way';

export class RouteData {
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly relation: RawRelation;
  readonly name: string;
  readonly networkNodes: List<RawNode>;
  readonly nodes: List<RawNode>;
  readonly ways: List<RawWay>;
  readonly relations: List<RawRelation>;
  readonly facts: List<Fact>;

  constructor(country: Country,
              networkType: NetworkType,
              relation: RawRelation,
              name: string,
              networkNodes: List<RawNode>,
              nodes: List<RawNode>,
              ways: List<RawWay>,
              relations: List<RawRelation>,
              facts: List<Fact>) {
    this.country = country;
    this.networkType = networkType;
    this.relation = relation;
    this.name = name;
    this.networkNodes = networkNodes;
    this.nodes = nodes;
    this.ways = ways;
    this.relations = relations;
    this.facts = facts;
  }

  public static fromJSON(jsonObject): RouteData {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteData(
      Country.fromJSON(jsonObject.country),
      NetworkType.fromJSON(jsonObject.networkType),
      RawRelation.fromJSON(jsonObject.relation),
      jsonObject.name,
      jsonObject.networkNodes ? List(jsonObject.networkNodes.map(json => RawNode.fromJSON(json))) : List(),
      jsonObject.nodes ? List(jsonObject.nodes.map(json => RawNode.fromJSON(json))) : List(),
      jsonObject.ways ? List(jsonObject.ways.map(json => RawWay.fromJSON(json))) : List(),
      jsonObject.relations ? List(jsonObject.relations.map(json => RawRelation.fromJSON(json))) : List(),
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List()
    );
  }
}
