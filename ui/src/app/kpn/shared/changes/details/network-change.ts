// this class is generated, please do not modify

import {ChangeKey} from './change-key';
import {ChangeType} from './change-type';
import {Country} from '../../country';
import {IdDiffs} from '../../diff/id-diffs';
import {NetworkDataUpdate} from '../../diff/network-data-update';
import {NetworkType} from '../../network-type';
import {RefChanges} from './ref-changes';
import {RefDiffs} from '../../diff/ref-diffs';

export class NetworkChange {

  constructor(public key?: ChangeKey,
              public changeType?: ChangeType,
              public country?: Country,
              public networkType?: NetworkType,
              public networkId?: number,
              public networkName?: string,
              public orphanRoutes?: RefChanges,
              public ignoredRoutes?: RefChanges,
              public orphanNodes?: RefChanges,
              public ignoredNodes?: RefChanges,
              public networkDataUpdate?: NetworkDataUpdate,
              public networkNodes?: RefDiffs,
              public routes?: RefDiffs,
              public nodes?: IdDiffs,
              public ways?: IdDiffs,
              public relations?: IdDiffs,
              public happy?: boolean,
              public investigate?: boolean) {
  }

  public static fromJSON(jsonObject): NetworkChange {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkChange();
    instance.key = jsonObject.key;
    instance.changeType = jsonObject.changeType;
    instance.country = jsonObject.country;
    instance.networkType = jsonObject.networkType;
    instance.networkId = jsonObject.networkId;
    instance.networkName = jsonObject.networkName;
    instance.orphanRoutes = jsonObject.orphanRoutes;
    instance.ignoredRoutes = jsonObject.ignoredRoutes;
    instance.orphanNodes = jsonObject.orphanNodes;
    instance.ignoredNodes = jsonObject.ignoredNodes;
    instance.networkDataUpdate = jsonObject.networkDataUpdate;
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

