// this class is generated, please do not modify

import {NetworkAttributes} from './network-attributes';
import {NetworkFacts} from '../network-facts';
import {NetworkSummary} from './network-summary';
import {Tags} from '../data/tags';

export class NetworkDetailsPage {

  constructor(public networkSummary?: NetworkSummary,
              public active?: boolean,
              public ignored?: boolean,
              public attributes?: NetworkAttributes,
              public tags?: Tags,
              public facts?: NetworkFacts) {
  }

  public static fromJSON(jsonObject): NetworkDetailsPage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkDetailsPage();
    instance.networkSummary = NetworkSummary.fromJSON(jsonObject.networkSummary);
    instance.active = jsonObject.active;
    instance.ignored = jsonObject.ignored;
    instance.attributes = NetworkAttributes.fromJSON(jsonObject.attributes);
    instance.tags = Tags.fromJSON(jsonObject.tags);
    instance.facts = NetworkFacts.fromJSON(jsonObject.facts);
    return instance;
  }
}

