// this class is generated, please do not modify

import {List} from 'immutable';
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

  constructor(readonly id: number,
              readonly version: number,
              readonly changeKey: ChangeKey,
              readonly comment: string,
              readonly before: MetaData,
              readonly after: MetaData,
              readonly removedWays: List<WayInfo>,
              readonly addedWays: List<WayInfo>,
              readonly updatedWays: List<WayUpdate>,
              readonly diffs: RouteDiff,
              readonly nodes: List<RawNode>,
              readonly changeSetInfo: ChangeSetInfo,
              readonly addedNodes: List<number>,
              readonly deletedNodes: List<number>,
              readonly commonNodes: List<number>,
              readonly addedWayIds: List<number>,
              readonly deletedWayIds: List<number>,
              readonly commonWayIds: List<number>,
              readonly geometryDiff: GeometryDiff,
              readonly bounds: Bounds,
              readonly happy: boolean,
              readonly investigate: boolean) {
  }

  public static fromJSON(jsonObject): RouteChangeInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteChangeInfo(
      jsonObject.id,
      jsonObject.version,
      ChangeKey.fromJSON(jsonObject.changeKey),
      jsonObject.comment,
      MetaData.fromJSON(jsonObject.before),
      MetaData.fromJSON(jsonObject.after),
      jsonObject.removedWays ? List(jsonObject.removedWays.map(json => WayInfo.fromJSON(json))) : List(),
      jsonObject.addedWays ? List(jsonObject.addedWays.map(json => WayInfo.fromJSON(json))) : List(),
      jsonObject.updatedWays ? List(jsonObject.updatedWays.map(json => WayUpdate.fromJSON(json))) : List(),
      RouteDiff.fromJSON(jsonObject.diffs),
      jsonObject.nodes ? List(jsonObject.nodes.map(json => RawNode.fromJSON(json))) : List(),
      ChangeSetInfo.fromJSON(jsonObject.changeSetInfo),
      jsonObject.addedNodes ? List(jsonObject.addedNodes) : List(),
      jsonObject.deletedNodes ? List(jsonObject.deletedNodes) : List(),
      jsonObject.commonNodes ? List(jsonObject.commonNodes) : List(),
      jsonObject.addedWayIds ? List(jsonObject.addedWayIds) : List(),
      jsonObject.deletedWayIds ? List(jsonObject.deletedWayIds) : List(),
      jsonObject.commonWayIds ? List(jsonObject.commonWayIds) : List(),
      GeometryDiff.fromJSON(jsonObject.geometryDiff),
      Bounds.fromJSON(jsonObject.bounds),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
