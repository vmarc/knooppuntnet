// this class is generated, please do not modify

import { Fact } from '../../custom/fact';
import { NetworkFactRefs } from './network-fact-refs';
import { SubsetInfo } from './subset-info';

export class SubsetNodeFactDetailsPage {
  constructor(
    readonly subsetInfo: SubsetInfo,
    readonly fact: Fact,
    readonly networks: Array<NetworkFactRefs>
  ) {}

  public static fromJSON(jsonObject: any): SubsetNodeFactDetailsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetNodeFactDetailsPage(
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      jsonObject.fact,
      jsonObject.networks.map((json: any) => NetworkFactRefs.fromJSON(json))
    );
  }
}
