// this class is generated, please do not modify

import { Fact } from '../../custom/fact';
import { Tags } from '../../custom/tags';
import { NetworkAttributes } from './network-attributes';
import { NetworkInfoDetail } from './network-info-detail';

export class NetworkInfo {
  constructor(
    readonly attributes: NetworkAttributes,
    readonly active: boolean,
    readonly nodeRefs: Array<number>,
    readonly routeRefs: Array<number>,
    readonly networkRefs: Array<number>,
    readonly facts: Array<Fact>,
    readonly tags: Tags,
    readonly detail: NetworkInfoDetail
  ) {}

  public static fromJSON(jsonObject: any): NetworkInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkInfo(
      NetworkAttributes.fromJSON(jsonObject.attributes),
      jsonObject.active,
      jsonObject.nodeRefs,
      jsonObject.routeRefs,
      jsonObject.networkRefs,
      jsonObject.facts,
      Tags.fromJSON(jsonObject.tags),
      NetworkInfoDetail.fromJSON(jsonObject.detail)
    );
  }
}
