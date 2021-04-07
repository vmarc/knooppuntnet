// this class is generated, please do not modify

import { ChangeSetElementRefs } from './change-set-element-refs';
import { Country } from '../custom/country';
import { NetworkType } from '../custom/network-type';

export class ChangeSetNetwork {
  constructor(
    readonly country: Country,
    readonly networkType: NetworkType,
    readonly networkId: number,
    readonly networkName: string,
    readonly routeChanges: ChangeSetElementRefs,
    readonly nodeChanges: ChangeSetElementRefs,
    readonly happy: boolean,
    readonly investigate: boolean
  ) {}

  public static fromJSON(jsonObject: any): ChangeSetNetwork {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetNetwork(
      jsonObject.country,
      jsonObject.networkType,
      jsonObject.networkId,
      jsonObject.networkName,
      ChangeSetElementRefs.fromJSON(jsonObject.routeChanges),
      ChangeSetElementRefs.fromJSON(jsonObject.nodeChanges),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
