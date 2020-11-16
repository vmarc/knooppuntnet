// this class is generated, please do not modify

import {List} from 'immutable';
import {NetworkFact} from '../network-fact';
import {NetworkSummary} from './network-summary';

export class NetworkFactsPage {

  constructor(readonly networkSummary: NetworkSummary,
              readonly facts: List<NetworkFact>) {
  }

  public static fromJSON(jsonObject: any): NetworkFactsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkFactsPage(
      NetworkSummary.fromJSON(jsonObject.networkSummary),
      jsonObject.facts ? List(jsonObject.facts.map((json: any) => NetworkFact.fromJSON(json))) : List()
    );
  }
}
