// this class is generated, please do not modify

import {ChangeKey} from './change-key';
import {ChangeType} from './change-type';
import {Country} from '../../country';
import {IdDiffs} from '../../diff/id-diffs';
import {MetaData} from '../../data/meta-data';
import {NetworkType} from '../../network-type';
import {RefChanges} from './ref-changes';
import {RefDiffs} from '../../diff/ref-diffs';

export class NetworkChangeInfo {
  readonly comment: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly networkId: number;
  readonly networkName: string;
  readonly before: MetaData;
  readonly after: MetaData;
  readonly orphanRoutes: RefChanges;
  readonly ignoredRoutes: RefChanges;
  readonly orphanNodes: RefChanges;
  readonly ignoredNodes: RefChanges;
  readonly networkDataUpdated: boolean;
  readonly networkNodes: RefDiffs;
  readonly routes: RefDiffs;
  readonly nodes: IdDiffs;
  readonly ways: IdDiffs;
  readonly relations: IdDiffs;
  readonly happy: boolean;
  readonly investigate: boolean;

  constructor(comment: string,
              key: ChangeKey,
              changeType: ChangeType,
              country: Country,
              networkType: NetworkType,
              networkId: number,
              networkName: string,
              before: MetaData,
              after: MetaData,
              orphanRoutes: RefChanges,
              ignoredRoutes: RefChanges,
              orphanNodes: RefChanges,
              ignoredNodes: RefChanges,
              networkDataUpdated: boolean,
              networkNodes: RefDiffs,
              routes: RefDiffs,
              nodes: IdDiffs,
              ways: IdDiffs,
              relations: IdDiffs,
              happy: boolean,
              investigate: boolean) {
    this.comment = comment;
    this.key = key;
    this.changeType = changeType;
    this.country = country;
    this.networkType = networkType;
    this.networkId = networkId;
    this.networkName = networkName;
    this.before = before;
    this.after = after;
    this.orphanRoutes = orphanRoutes;
    this.ignoredRoutes = ignoredRoutes;
    this.orphanNodes = orphanNodes;
    this.ignoredNodes = ignoredNodes;
    this.networkDataUpdated = networkDataUpdated;
    this.networkNodes = networkNodes;
    this.routes = routes;
    this.nodes = nodes;
    this.ways = ways;
    this.relations = relations;
    this.happy = happy;
    this.investigate = investigate;
  }

  public static fromJSON(jsonObject): NetworkChangeInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkChangeInfo(
      jsonObject.comment,
      ChangeKey.fromJSON(jsonObject.key),
      ChangeType.fromJSON(jsonObject.changeType),
      Country.fromJSON(jsonObject.country),
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.networkId,
      jsonObject.networkName,
      MetaData.fromJSON(jsonObject.before),
      MetaData.fromJSON(jsonObject.after),
      RefChanges.fromJSON(jsonObject.orphanRoutes),
      RefChanges.fromJSON(jsonObject.ignoredRoutes),
      RefChanges.fromJSON(jsonObject.orphanNodes),
      RefChanges.fromJSON(jsonObject.ignoredNodes),
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
