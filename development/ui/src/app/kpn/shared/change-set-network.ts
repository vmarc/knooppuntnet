// this class is generated, please do not modify

import {ChangeSetElementRefs} from './change-set-element-refs';
import {Country} from './country';
import {NetworkType} from './network-type';

export class ChangeSetNetwork {
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly networkId: number;
  readonly networkName: string;
  readonly routeChanges: ChangeSetElementRefs;
  readonly nodeChanges: ChangeSetElementRefs;
  readonly happy: boolean;
  readonly investigate: boolean;

  constructor(country: Country,
              networkType: NetworkType,
              networkId: number,
              networkName: string,
              routeChanges: ChangeSetElementRefs,
              nodeChanges: ChangeSetElementRefs,
              happy: boolean,
              investigate: boolean) {
    this.country = country;
    this.networkType = networkType;
    this.networkId = networkId;
    this.networkName = networkName;
    this.routeChanges = routeChanges;
    this.nodeChanges = nodeChanges;
    this.happy = happy;
    this.investigate = investigate;
  }

  public static fromJSON(jsonObject): ChangeSetNetwork {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetNetwork(
      Country.fromJSON(jsonObject.country),
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.networkId,
      jsonObject.networkName,
      ChangeSetElementRefs.fromJSON(jsonObject.routeChanges),
      ChangeSetElementRefs.fromJSON(jsonObject.nodeChanges),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
