// this class is generated, please do not modify

import {Bounds} from '../bounds';
import {SubsetInfo} from './subset-info';
import {SubsetMapNetwork} from './subset-map-network';

export class SubsetMapPage {

  constructor(readonly subsetInfo: SubsetInfo,
              readonly networks: Array<SubsetMapNetwork>,
              readonly bounds: Bounds) {
  }

  public static fromJSON(jsonObject: any): SubsetMapPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetMapPage(
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      jsonObject.networks.map((json: any) => SubsetMapNetwork.fromJSON(json)),
      Bounds.fromJSON(jsonObject.bounds)
    );
  }
}
