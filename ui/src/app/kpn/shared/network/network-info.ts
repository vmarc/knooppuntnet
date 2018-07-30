// this class is generated, please do not modify

import {Fact} from '../fact';
import {NetworkAttributes} from './network-attributes';
import {NetworkInfoDetail} from './network-info-detail';
import {Tags} from '../data/tags';

export class NetworkInfo {

  constructor(public attributes?: NetworkAttributes,
              public active?: boolean,
              public ignored?: boolean,
              public nodeRefs?: Array<number>,
              public routeRefs?: Array<number>,
              public networkRefs?: Array<number>,
              public facts?: Array<Fact>,
              public tags?: Tags,
              public detail?: NetworkInfoDetail) {
  }

  public static fromJSON(jsonObject): NetworkInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkInfo();
    instance.attributes = jsonObject.attributes;
    instance.active = jsonObject.active;
    instance.ignored = jsonObject.ignored;
    instance.nodeRefs = jsonObject.nodeRefs;
    instance.routeRefs = jsonObject.routeRefs;
    instance.networkRefs = jsonObject.networkRefs;
    instance.facts = jsonObject.facts ? jsonObject.facts.map(json => Fact.fromJSON(json)) : [];
    instance.tags = jsonObject.tags;
    instance.detail = jsonObject.detail;
    return instance;
  }
}

