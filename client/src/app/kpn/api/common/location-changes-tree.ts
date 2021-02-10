// this class is generated, please do not modify

import {LocationChangesTreeNode} from './location-changes-tree-node';
import {NetworkType} from '../custom/network-type';

export class LocationChangesTree {

  constructor(readonly networkType: NetworkType,
              readonly locationName: string,
              readonly happy: boolean,
              readonly investigate: boolean,
              readonly children: Array<LocationChangesTreeNode>) {
  }

  public static fromJSON(jsonObject: any): LocationChangesTree {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationChangesTree(
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.locationName,
      jsonObject.happy,
      jsonObject.investigate,
      jsonObject.children?.map((json: any) => LocationChangesTreeNode.fromJSON(json))
    );
  }
}
