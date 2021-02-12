// this class is generated, please do not modify

import {Fact} from '../../custom/fact';
import {NetworkFactRefs} from './network-fact-refs';
import {SubsetInfo} from './subset-info';

export class SubsetFactDetailsPage {

  constructor(readonly subsetInfo: SubsetInfo,
              readonly fact: Fact,
              readonly networks: Array<NetworkFactRefs>) {
  }

  public static fromJSON(jsonObject: any): SubsetFactDetailsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetFactDetailsPage(
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      Fact.fromJSON(jsonObject.fact),
      jsonObject.networks.map((json: any) => NetworkFactRefs.fromJSON(json))
    );
  }
}
