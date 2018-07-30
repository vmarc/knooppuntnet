// this class is generated, please do not modify

import {Bounds} from '../bounds';
import {ChangeKey} from '../changes/details/change-key';
import {ChangeSetInfo} from '../changes/change-set-info';
import {GeometryDiff} from './geometry-diff';
import {MetaData} from '../data/meta-data';
import {RawNode} from '../data/raw/raw-node';
import {RouteDiff} from '../diff/route/route-diff';
import {WayInfo} from '../diff/way-info';
import {WayUpdate} from '../diff/way-update';

export class RouteChangeInfo {

  constructor(public id?: number,
              public version?: number,
              public changeKey?: ChangeKey,
              public comment?: string,
              public before?: MetaData,
              public after?: MetaData,
              public removedWays?: Array<WayInfo>,
              public addedWays?: Array<WayInfo>,
              public updatedWays?: Array<WayUpdate>,
              public diffs?: RouteDiff,
              public nodes?: Array<RawNode>,
              public changeSetInfo?: ChangeSetInfo,
              public addedNodes?: Array<number>,
              public deletedNodes?: Array<number>,
              public commonNodes?: Array<number>,
              public addedWayIds?: Array<number>,
              public deletedWayIds?: Array<number>,
              public commonWayIds?: Array<number>,
              public geometryDiff?: GeometryDiff,
              public bounds?: Bounds,
              public happy?: boolean,
              public investigate?: boolean) {
  }

  public static fromJSON(jsonObject): RouteChangeInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteChangeInfo();
    instance.id = jsonObject.id;
    instance.version = jsonObject.version;
    instance.changeKey = jsonObject.changeKey;
    instance.comment = jsonObject.comment;
    instance.before = jsonObject.before;
    instance.after = jsonObject.after;
    instance.removedWays = jsonObject.removedWays ? jsonObject.removedWays.map(json => WayInfo.fromJSON(json)) : [];
    instance.addedWays = jsonObject.addedWays ? jsonObject.addedWays.map(json => WayInfo.fromJSON(json)) : [];
    instance.updatedWays = jsonObject.updatedWays ? jsonObject.updatedWays.map(json => WayUpdate.fromJSON(json)) : [];
    instance.diffs = jsonObject.diffs;
    instance.nodes = jsonObject.nodes ? jsonObject.nodes.map(json => RawNode.fromJSON(json)) : [];
    instance.changeSetInfo = jsonObject.changeSetInfo;
    instance.addedNodes = jsonObject.addedNodes;
    instance.deletedNodes = jsonObject.deletedNodes;
    instance.commonNodes = jsonObject.commonNodes;
    instance.addedWayIds = jsonObject.addedWayIds;
    instance.deletedWayIds = jsonObject.deletedWayIds;
    instance.commonWayIds = jsonObject.commonWayIds;
    instance.geometryDiff = jsonObject.geometryDiff;
    instance.bounds = jsonObject.bounds;
    instance.happy = jsonObject.happy;
    instance.investigate = jsonObject.investigate;
    return instance;
  }
}

