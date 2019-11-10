// this class is generated, please do not modify

import {List} from "immutable";
import {Fact} from "../../custom/fact";
import {NetworkFactRefs} from "./network-fact-refs";
import {SubsetInfo} from "./subset-info";

export class SubsetFactDetailsPage {

  constructor(readonly subsetInfo: SubsetInfo,
              readonly fact: Fact,
              readonly networks: List<NetworkFactRefs>) {
  }

  public static fromJSON(jsonObject): SubsetFactDetailsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetFactDetailsPage(
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      Fact.fromJSON(jsonObject.fact),
      jsonObject.networks ? List(jsonObject.networks.map(json => NetworkFactRefs.fromJSON(json))) : List()
    );
  }
}
