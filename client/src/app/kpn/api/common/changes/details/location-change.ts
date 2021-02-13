// this class is generated, please do not modify

import {ChangeKey} from './change-key';
import {NetworkType} from '../../../custom/network-type';
import {RefDiffs} from '../../diff/ref-diffs';

export class LocationChange {

  constructor(readonly key: ChangeKey,
              readonly networkType: NetworkType,
              readonly locationName: string,
              readonly nodes: RefDiffs,
              readonly routes: RefDiffs,
              readonly happy: boolean,
              readonly investigate: boolean) {
  }

  public static fromJSON(jsonObject: any): LocationChange {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationChange(
      ChangeKey.fromJSON(jsonObject.key),
      jsonObject.networkType,
      jsonObject.locationName,
      RefDiffs.fromJSON(jsonObject.nodes),
      RefDiffs.fromJSON(jsonObject.routes),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
