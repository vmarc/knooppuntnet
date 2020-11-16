// this class is generated, please do not modify

import {List} from 'immutable';
import {Fact} from '../../custom/fact';
import {NetworkAttributes} from './network-attributes';
import {NetworkInfoDetail} from './network-info-detail';
import {Tags} from '../../custom/tags';

export class NetworkInfo {

  constructor(readonly attributes: NetworkAttributes,
              readonly active: boolean,
              readonly nodeRefs: List<number>,
              readonly routeRefs: List<number>,
              readonly networkRefs: List<number>,
              readonly facts: List<Fact>,
              readonly tags: Tags,
              readonly detail: NetworkInfoDetail) {
  }

  public static fromJSON(jsonObject: any): NetworkInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkInfo(
      NetworkAttributes.fromJSON(jsonObject.attributes),
      jsonObject.active,
      jsonObject.nodeRefs ? List(jsonObject.nodeRefs) : List(),
      jsonObject.routeRefs ? List(jsonObject.routeRefs) : List(),
      jsonObject.networkRefs ? List(jsonObject.networkRefs) : List(),
      jsonObject.facts ? List(jsonObject.facts.map((json: any) => Fact.fromJSON(json))) : List(),
      Tags.fromJSON(jsonObject.tags),
      NetworkInfoDetail.fromJSON(jsonObject.detail)
    );
  }
}
