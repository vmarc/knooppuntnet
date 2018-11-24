// this class is generated, please do not modify

import {List} from 'immutable';
import {Fact} from '../fact';
import {NodeIntegrityCheck} from '../node-integrity-check';
import {Ref} from '../common/ref';
import {Tags} from '../data/tags';
import {Timestamp} from '../timestamp';

export class NetworkNodeInfo2 {
  readonly id: number;
  readonly name: string;
  readonly number: string;
  readonly latitude: string;
  readonly longitude: string;
  readonly connection: boolean;
  readonly roleConnection: boolean;
  readonly definedInRelation: boolean;
  readonly definedInRoute: boolean;
  readonly timestamp: Timestamp;
  readonly routeReferences: List<Ref>;
  readonly integrityCheck: NodeIntegrityCheck;
  readonly facts: List<Fact>;
  readonly tags: Tags;

  constructor(id: number,
              name: string,
              number: string,
              latitude: string,
              longitude: string,
              connection: boolean,
              roleConnection: boolean,
              definedInRelation: boolean,
              definedInRoute: boolean,
              timestamp: Timestamp,
              routeReferences: List<Ref>,
              integrityCheck: NodeIntegrityCheck,
              facts: List<Fact>,
              tags: Tags) {
    this.id = id;
    this.name = name;
    this.number = number;
    this.latitude = latitude;
    this.longitude = longitude;
    this.connection = connection;
    this.roleConnection = roleConnection;
    this.definedInRelation = definedInRelation;
    this.definedInRoute = definedInRoute;
    this.timestamp = timestamp;
    this.routeReferences = routeReferences;
    this.integrityCheck = integrityCheck;
    this.facts = facts;
    this.tags = tags;
  }

  public static fromJSON(jsonObject): NetworkNodeInfo2 {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkNodeInfo2(
      jsonObject.id,
      jsonObject.name,
      jsonObject.number,
      jsonObject.latitude,
      jsonObject.longitude,
      jsonObject.connection,
      jsonObject.roleConnection,
      jsonObject.definedInRelation,
      jsonObject.definedInRoute,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.routeReferences ? List(jsonObject.routeReferences.map(json => Ref.fromJSON(json))) : List(),
      NodeIntegrityCheck.fromJSON(jsonObject.integrityCheck),
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List(),
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
