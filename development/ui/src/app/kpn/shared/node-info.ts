// this class is generated, please do not modify

import {List} from 'immutable';
import {Country} from './country';
import {Fact} from './fact';
import {Tags} from './data/tags';
import {Timestamp} from './timestamp';

export class NodeInfo {
  readonly id: number;
  readonly active: boolean;
  readonly display: boolean;
  readonly ignored: boolean;
  readonly orphan: boolean;
  readonly country: Country;
  readonly name: string;
  readonly rcnName: string;
  readonly rwnName: string;
  readonly rhnName: string;
  readonly rmnName: string;
  readonly rpnName: string;
  readonly rinName: string;
  readonly latitude: string;
  readonly longitude: string;
  readonly lastUpdated: Timestamp;
  readonly tags: Tags;
  readonly facts: List<Fact>;

  constructor(id: number,
              active: boolean,
              display: boolean,
              ignored: boolean,
              orphan: boolean,
              country: Country,
              name: string,
              rcnName: string,
              rwnName: string,
              rhnName: string,
              rmnName: string,
              rpnName: string,
              rinName: string,
              latitude: string,
              longitude: string,
              lastUpdated: Timestamp,
              tags: Tags,
              facts: List<Fact>) {
    this.id = id;
    this.active = active;
    this.display = display;
    this.ignored = ignored;
    this.orphan = orphan;
    this.country = country;
    this.name = name;
    this.rcnName = rcnName;
    this.rwnName = rwnName;
    this.rhnName = rhnName;
    this.rmnName = rmnName;
    this.rpnName = rpnName;
    this.rinName = rinName;
    this.latitude = latitude;
    this.longitude = longitude;
    this.lastUpdated = lastUpdated;
    this.tags = tags;
    this.facts = facts;
  }

  public static fromJSON(jsonObject): NodeInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeInfo(
      jsonObject.id,
      jsonObject.active,
      jsonObject.display,
      jsonObject.ignored,
      jsonObject.orphan,
      Country.fromJSON(jsonObject.country),
      jsonObject.name,
      jsonObject.rcnName,
      jsonObject.rwnName,
      jsonObject.rhnName,
      jsonObject.rmnName,
      jsonObject.rpnName,
      jsonObject.rinName,
      jsonObject.latitude,
      jsonObject.longitude,
      Timestamp.fromJSON(jsonObject.lastUpdated),
      Tags.fromJSON(jsonObject.tags),
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List()
    );
  }
}
