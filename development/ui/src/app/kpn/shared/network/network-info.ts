// this class is generated, please do not modify

import {List} from 'immutable';
import {Fact} from '../fact';
import {NetworkAttributes} from './network-attributes';
import {NetworkInfoDetail} from './network-info-detail';
import {Tags} from '../data/tags';

export class NetworkInfo {
  readonly attributes: NetworkAttributes;
  readonly active: boolean;
  readonly ignored: boolean;
  readonly nodeRefs: List<number>;
  readonly routeRefs: List<number>;
  readonly networkRefs: List<number>;
  readonly facts: List<Fact>;
  readonly tags: Tags;
  readonly detail: NetworkInfoDetail;

  constructor(attributes: NetworkAttributes,
              active: boolean,
              ignored: boolean,
              nodeRefs: List<number>,
              routeRefs: List<number>,
              networkRefs: List<number>,
              facts: List<Fact>,
              tags: Tags,
              detail: NetworkInfoDetail) {
    this.attributes = attributes;
    this.active = active;
    this.ignored = ignored;
    this.nodeRefs = nodeRefs;
    this.routeRefs = routeRefs;
    this.networkRefs = networkRefs;
    this.facts = facts;
    this.tags = tags;
    this.detail = detail;
  }

  public static fromJSON(jsonObject): NetworkInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkInfo(
      NetworkAttributes.fromJSON(jsonObject.attributes),
      jsonObject.active,
      jsonObject.ignored,
      jsonObject.nodeRefs ? List(jsonObject.nodeRefs) : List(),
      jsonObject.routeRefs ? List(jsonObject.routeRefs) : List(),
      jsonObject.networkRefs ? List(jsonObject.networkRefs) : List(),
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List(),
      Tags.fromJSON(jsonObject.tags),
      NetworkInfoDetail.fromJSON(jsonObject.detail)
    );
  }
}
