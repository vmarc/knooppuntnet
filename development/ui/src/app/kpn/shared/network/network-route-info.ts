// this class is generated, please do not modify

import {List} from 'immutable';
import {Fact} from '../fact';
import {Timestamp} from '../timestamp';

export class NetworkRouteInfo {
  readonly id: number;
  readonly name: string;
  readonly wayCount: number;
  readonly length: number;
  readonly role: string;
  readonly relationLastUpdated: Timestamp;
  readonly lastUpdated: Timestamp;
  readonly facts: List<Fact>;

  constructor(id: number,
              name: string,
              wayCount: number,
              length: number,
              role: string,
              relationLastUpdated: Timestamp,
              lastUpdated: Timestamp,
              facts: List<Fact>) {
    this.id = id;
    this.name = name;
    this.wayCount = wayCount;
    this.length = length;
    this.role = role;
    this.relationLastUpdated = relationLastUpdated;
    this.lastUpdated = lastUpdated;
    this.facts = facts;
  }

  public static fromJSON(jsonObject): NetworkRouteInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkRouteInfo(
      jsonObject.id,
      jsonObject.name,
      jsonObject.wayCount,
      jsonObject.length,
      jsonObject.role,
      Timestamp.fromJSON(jsonObject.relationLastUpdated),
      Timestamp.fromJSON(jsonObject.lastUpdated),
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List()
    );
  }
}
