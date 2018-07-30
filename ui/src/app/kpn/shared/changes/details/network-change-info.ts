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

  constructor(public comment?: string,
              public key?: ChangeKey,
              public changeType?: ChangeType,
              public country?: Country,
              public networkType?: NetworkType,
              public networkId?: number,
              public networkName?: string,
              public before?: MetaData,
              public after?: MetaData,
              public orphanRoutes?: RefChanges,
              public ignoredRoutes?: RefChanges,
              public orphanNodes?: RefChanges,
              public ignoredNodes?: RefChanges,
              public networkDataUpdated?: boolean,
              public networkNodes?: RefDiffs,
              public routes?: RefDiffs,
              public nodes?: IdDiffs,
              public ways?: IdDiffs,
              public relations?: IdDiffs,
              public happy?: boolean,
              public investigate?: boolean) {
  }

  public static fromJSON(jsonObject): NetworkChangeInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkChangeInfo();
    instance.comment = jsonObject.comment;
    instance.key = jsonObject.key;
    instance.changeType = jsonObject.changeType;
    instance.country = jsonObject.country;
    instance.networkType = jsonObject.networkType;
    instance.networkId = jsonObject.networkId;
    instance.networkName = jsonObject.networkName;
    instance.before = jsonObject.before;
    instance.after = jsonObject.after;
    instance.orphanRoutes = jsonObject.orphanRoutes;
    instance.ignoredRoutes = jsonObject.ignoredRoutes;
    instance.orphanNodes = jsonObject.orphanNodes;
    instance.ignoredNodes = jsonObject.ignoredNodes;
    instance.networkDataUpdated = jsonObject.networkDataUpdated;
    instance.networkNodes = jsonObject.networkNodes;
    instance.routes = jsonObject.routes;
    instance.nodes = jsonObject.nodes;
    instance.ways = jsonObject.ways;
    instance.relations = jsonObject.relations;
    instance.happy = jsonObject.happy;
    instance.investigate = jsonObject.investigate;
    return instance;
  }
}

