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
    instance.key = ChangeKey.fromJSON(jsonObject.key);
    instance.changeType = ChangeType.fromJSON(jsonObject.changeType);
    instance.country = Country.fromJSON(jsonObject.country);
    instance.networkType = NetworkType.fromJSON(jsonObject.networkType);
    instance.networkId = jsonObject.networkId;
    instance.networkName = jsonObject.networkName;
    instance.orphanRoutes = RefChanges.fromJSON(jsonObject.orphanRoutes);
    instance.ignoredRoutes = RefChanges.fromJSON(jsonObject.ignoredRoutes);
    instance.orphanNodes = RefChanges.fromJSON(jsonObject.orphanNodes);
    instance.ignoredNodes = RefChanges.fromJSON(jsonObject.ignoredNodes);
    instance.networkDataUpdate = NetworkDataUpdate.fromJSON(jsonObject.networkDataUpdate);
    instance.networkNodes = RefDiffs.fromJSON(jsonObject.networkNodes);
    instance.routes = RefDiffs.fromJSON(jsonObject.routes);
    instance.nodes = IdDiffs.fromJSON(jsonObject.nodes);
    instance.ways = IdDiffs.fromJSON(jsonObject.ways);
    instance.relations = IdDiffs.fromJSON(jsonObject.relations);
    instance.happy = jsonObject.happy;
    instance.investigate = jsonObject.investigate;
    return instance;
  }
}

