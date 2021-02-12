// this class is generated, please do not modify

import {FactCount} from '../fact-count';
import {SubsetInfo} from './subset-info';

export class SubsetFactsPage {

  constructor(readonly subsetInfo: SubsetInfo,
              readonly factCounts: Array<FactCount>) {
  }

  public static fromJSON(jsonObject: any): SubsetFactsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetFactsPage(
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      jsonObject.factCounts.map((json: any) => FactCount.fromJSON(json))
    );
  }
}
