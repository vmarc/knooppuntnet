// this class is generated, please do not modify

import {Fact} from '../fact';
import {NetworkFacts} from '../network-facts';
import {NetworkRouteFact} from './network-route-fact';
import {NetworkSummary} from './network-summary';

export class NetworkFactsPage {

  constructor(public networkSummary?: NetworkSummary,
              public networkFacts?: NetworkFacts,
              public routeFacts?: Array<NetworkRouteFact>,
              public facts?: Array<Fact>) {
  }

  public static fromJSON(jsonObject): NetworkFactsPage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkFactsPage();
    instance.networkSummary = jsonObject.networkSummary;
    instance.networkFacts = jsonObject.networkFacts;
    instance.routeFacts = jsonObject.routeFacts ? jsonObject.routeFacts.map(json => NetworkRouteFact.fromJSON(json)) : [];
    instance.facts = jsonObject.facts ? jsonObject.facts.map(json => Fact.fromJSON(json)) : [];
    return instance;
  }
}

