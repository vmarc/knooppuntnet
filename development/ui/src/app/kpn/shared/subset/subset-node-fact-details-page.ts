// this class is generated, please do not modify

import {List} from 'immutable';
import {Fact} from '../fact';
import {NetworkFactRefs} from './network-fact-refs';
import {SubsetInfo} from './subset-info';

export class SubsetNodeFactDetailsPage {

  constructor(readonly subsetInfo: SubsetInfo,
              readonly fact: Fact,
              readonly networks: List<NetworkFactRefs>) {
  }

  public static fromJSON(jsonObject): SubsetNodeFactDetailsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetNodeFactDetailsPage(
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      Fact.fromJSON(jsonObject.fact),
      jsonObject.networks ? List(jsonObject.networks.map(json => NetworkFactRefs.fromJSON(json))) : List()
    );
  }
}
