// this class is generated, please do not modify

import {List} from "immutable";
import {FactCountNew} from "../fact-count-new";
import {SubsetInfo} from "./subset-info";

export class SubsetFactsPageNew {

  constructor(readonly subsetInfo: SubsetInfo,
              readonly factCounts: List<FactCountNew>) {
  }

  public static fromJSON(jsonObject): SubsetFactsPageNew {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetFactsPageNew(
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      jsonObject.factCounts ? List(jsonObject.factCounts.map(json => FactCountNew.fromJSON(json))) : List()
    );
  }
}
