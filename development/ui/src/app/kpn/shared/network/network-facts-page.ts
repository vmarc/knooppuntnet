// this class is generated, please do not modify

import {List} from "immutable";
import {Fact} from "../fact";
import {NetworkFacts} from "../network-facts";
import {NetworkNodeFact} from "./network-node-fact";
import {NetworkRouteFact} from "./network-route-fact";
import {NetworkSummary} from "./network-summary";

export class NetworkFactsPage {

  constructor(readonly networkSummary: NetworkSummary,
              readonly networkFacts: NetworkFacts,
              readonly nodeFacts: List<NetworkNodeFact>,
              readonly routeFacts: List<NetworkRouteFact>,
              readonly facts: List<Fact>) {
  }

  public static fromJSON(jsonObject): NetworkFactsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkFactsPage(
      NetworkSummary.fromJSON(jsonObject.networkSummary),
      NetworkFacts.fromJSON(jsonObject.networkFacts),
      jsonObject.nodeFacts ? List(jsonObject.nodeFacts.map(json => NetworkNodeFact.fromJSON(json))) : List(),
      jsonObject.routeFacts ? List(jsonObject.routeFacts.map(json => NetworkRouteFact.fromJSON(json))) : List(),
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List()
    );
  }
}
