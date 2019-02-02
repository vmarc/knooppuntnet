// this class is generated, please do not modify

import {List} from 'immutable';
import {FactCount} from '../fact-count';
import {SubsetInfo} from './subset-info';

export class SubsetFactsPage {

  constructor(readonly subsetInfo: SubsetInfo,
              readonly factCounts: List<FactCount>) {
  }

  public static fromJSON(jsonObject): SubsetFactsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetFactsPage(
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      jsonObject.factCounts ? List(jsonObject.factCounts.map(json => FactCount.fromJSON(json))) : List()
    );
  }
}
