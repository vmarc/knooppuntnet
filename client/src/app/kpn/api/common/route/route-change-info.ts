// this class is generated, please do not modify

import {Bounds} from '../bounds';
import {ChangeSetInfo} from '../changes/change-set-info';
import {ChangeKey} from '../changes/details/change-key';
import {MetaData} from '../data/meta-data';
import {RawNode} from '../data/raw/raw-node';
import {RouteDiff} from '../diff/route/route-diff';
import {WayInfo} from '../diff/way-info';
import {WayUpdate} from '../diff/way-update';
import {GeometryDiff} from './geometry-diff';

export class RouteChangeInfo {

  constructor(readonly id: number,
              readonly version: number,
              readonly changeKey: ChangeKey,
              readonly comment: string,
              readonly before: MetaData,
              readonly after: MetaData,
              readonly removedWays: Array<WayInfo>,
              readonly addedWays: Array<WayInfo>,
              readonly updatedWays: Array<WayUpdate>,
              readonly diffs: RouteDiff,
              readonly nodes: Array<RawNode>,
              readonly changeSetInfo: ChangeSetInfo,
              readonly geometryDiff: GeometryDiff,
              readonly bounds: Bounds,
              readonly happy: boolean,
              readonly investigate: boolean) {
  }

  public static fromJSON(jsonObject: any): RouteChangeInfo {
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
      jsonObject.removedWays.map((json: any) => WayInfo.fromJSON(json)),
      jsonObject.addedWays.map((json: any) => WayInfo.fromJSON(json)),
      jsonObject.updatedWays.map((json: any) => WayUpdate.fromJSON(json)),
      RouteDiff.fromJSON(jsonObject.diffs),
      jsonObject.nodes.map((json: any) => RawNode.fromJSON(json)),
      ChangeSetInfo.fromJSON(jsonObject.changeSetInfo),
      GeometryDiff.fromJSON(jsonObject.geometryDiff),
      Bounds.fromJSON(jsonObject.bounds),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
