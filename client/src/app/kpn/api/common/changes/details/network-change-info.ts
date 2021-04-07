// this class is generated, please do not modify

import { ChangeKey } from './change-key';
import { ChangeType } from './change-type';
import { Country } from '../../../custom/country';
import { IdDiffs } from '../../diff/id-diffs';
import { MetaData } from '../../data/meta-data';
import { NetworkType } from '../../../custom/network-type';
import { RefChanges } from './ref-changes';
import { RefDiffs } from '../../diff/ref-diffs';

export class NetworkChangeInfo {
  constructor(
    readonly comment: string,
    readonly key: ChangeKey,
    readonly changeType: ChangeType,
    readonly country: Country,
    readonly networkType: NetworkType,
    readonly networkId: number,
    readonly networkName: string,
    readonly before: MetaData,
    readonly after: MetaData,
    readonly orphanRoutes: RefChanges,
    readonly orphanNodes: RefChanges,
    readonly networkDataUpdated: boolean,
    readonly networkNodes: RefDiffs,
    readonly routes: RefDiffs,
    readonly nodes: IdDiffs,
    readonly ways: IdDiffs,
    readonly relations: IdDiffs,
    readonly happy: boolean,
    readonly investigate: boolean
  ) {}

  static fromJSON(jsonObject: any): NetworkChangeInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkChangeInfo(
      jsonObject.comment,
      ChangeKey.fromJSON(jsonObject.key),
      ChangeType.fromJSON(jsonObject.changeType),
      jsonObject.country,
      jsonObject.networkType,
      jsonObject.networkId,
      jsonObject.networkName,
      MetaData.fromJSON(jsonObject.before),
      MetaData.fromJSON(jsonObject.after),
      RefChanges.fromJSON(jsonObject.orphanRoutes),
      RefChanges.fromJSON(jsonObject.orphanNodes),
      jsonObject.networkDataUpdated,
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
