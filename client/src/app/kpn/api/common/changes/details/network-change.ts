// this class is generated, please do not modify

import {ChangeKey} from './change-key';
import {ChangeType} from './change-type';
import {Country} from '../../../custom/country';
import {IdDiffs} from '../../diff/id-diffs';
import {NetworkDataUpdate} from '../../diff/network-data-update';
import {NetworkType} from '../../../custom/network-type';
import {RefChanges} from './ref-changes';
import {RefDiffs} from '../../diff/ref-diffs';

export class NetworkChange {

  constructor(readonly key: ChangeKey,
              readonly changeType: ChangeType,
              readonly country: Country,
              readonly networkType: NetworkType,
              readonly networkId: number,
              readonly networkName: string,
              readonly orphanRoutes: RefChanges,
              readonly orphanNodes: RefChanges,
              readonly networkDataUpdate: NetworkDataUpdate,
              readonly networkNodes: RefDiffs,
              readonly routes: RefDiffs,
              readonly nodes: IdDiffs,
              readonly ways: IdDiffs,
              readonly relations: IdDiffs,
              readonly happy: boolean,
              readonly investigate: boolean) {
  }

  static fromJSON(jsonObject: any): NetworkChange {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkChange(
      ChangeKey.fromJSON(jsonObject.key),
      ChangeType.fromJSON(jsonObject.changeType),
      jsonObject.country,
      jsonObject.networkType,
      jsonObject.networkId,
      jsonObject.networkName,
      RefChanges.fromJSON(jsonObject.orphanRoutes),
      RefChanges.fromJSON(jsonObject.orphanNodes),
      NetworkDataUpdate.fromJSON(jsonObject.networkDataUpdate),
      RefDiffs.fromJSON(jsonObject.networkNodes),
      RefDiffs.fromJSON(jsonObject.routes),
      IdDiffs.fromJSON(jsonObject.nodes),
      IdDiffs.fromJSON(jsonObject.ways),
      IdDiffs.fromJSON(jsonObject.relations),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
