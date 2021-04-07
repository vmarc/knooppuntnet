// this class is generated, please do not modify

import { ChangeSetElementRefs } from './change-set-element-refs';
import { NetworkType } from '../custom/network-type';

export class ChangeSetLocation {
  constructor(
    readonly networkType: NetworkType,
    readonly locationName: string,
    readonly routeChanges: ChangeSetElementRefs,
    readonly nodeChanges: ChangeSetElementRefs,
    readonly happy: boolean,
    readonly investigate: boolean
  ) {}

  static fromJSON(jsonObject: any): ChangeSetLocation {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetLocation(
      jsonObject.networkType,
      jsonObject.locationName,
      ChangeSetElementRefs.fromJSON(jsonObject.routeChanges),
      ChangeSetElementRefs.fromJSON(jsonObject.nodeChanges),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
