// this class is generated, please do not modify

import {FactCount} from '../fact-count';
import {SubsetInfo} from './subset-info';

export class SubsetFactsPage {

  constructor(public subsetInfo?: SubsetInfo,
              public factCounts?: Array<FactCount>) {
  }

  public static fromJSON(jsonObject): SubsetFactsPage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new SubsetFactsPage();
    instance.subsetInfo = SubsetInfo.fromJSON(jsonObject.subsetInfo);
    instance.factCounts = jsonObject.factCounts ? jsonObject.factCounts.map(json => FactCount.fromJSON(json)) : [];
    return instance;
  }
}

