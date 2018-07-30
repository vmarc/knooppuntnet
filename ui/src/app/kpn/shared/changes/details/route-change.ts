// this class is generated, please do not modify

import {ChangeKey} from './change-key';
import {ChangeType} from './change-type';
import {Fact} from '../../fact';
import {RawWay} from '../../data/raw/raw-way';
import {Ref} from '../../common/ref';
import {RouteData} from '../../diff/route-data';
import {RouteDiff} from '../../diff/route/route-diff';
import {WayUpdate} from '../../diff/way-update';

export class RouteChange {

  constructor(public key?: ChangeKey,
              public changeType?: ChangeType,
              public name?: string,
              public addedToNetwork?: Array<Ref>,
              public removedFromNetwork?: Array<Ref>,
              public before?: RouteData,
              public after?: RouteData,
              public removedWays?: Array<RawWay>,
              public addedWays?: Array<RawWay>,
              public updatedWays?: Array<WayUpdate>,
              public diffs?: RouteDiff,
              public facts?: Array<Fact>) {
  }

  public static fromJSON(jsonObject): RouteChange {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteChange();
    instance.key = ChangeKey.fromJSON(jsonObject.key);
    instance.changeType = ChangeType.fromJSON(jsonObject.changeType);
    instance.name = jsonObject.name;
    instance.addedToNetwork = jsonObject.addedToNetwork ? jsonObject.addedToNetwork.map(json => Ref.fromJSON(json)) : [];
    instance.removedFromNetwork = jsonObject.removedFromNetwork ? jsonObject.removedFromNetwork.map(json => Ref.fromJSON(json)) : [];
    instance.before = RouteData.fromJSON(jsonObject.before);
    instance.after = RouteData.fromJSON(jsonObject.after);
    instance.removedWays = jsonObject.removedWays ? jsonObject.removedWays.map(json => RawWay.fromJSON(json)) : [];
    instance.addedWays = jsonObject.addedWays ? jsonObject.addedWays.map(json => RawWay.fromJSON(json)) : [];
    instance.updatedWays = jsonObject.updatedWays ? jsonObject.updatedWays.map(json => WayUpdate.fromJSON(json)) : [];
    instance.diffs = RouteDiff.fromJSON(jsonObject.diffs);
    instance.facts = jsonObject.facts ? jsonObject.facts.map(json => Fact.fromJSON(json)) : [];
    return instance;
  }
}

