// this class is generated, please do not modify

import { RouteLocationAnalysis } from '../route-location-analysis';
import { RouteMap } from './route-map';
import { RouteMemberInfo } from '../../custom/route-member-info';

export class RouteInfoAnalysis {
  constructor(
    readonly unexpectedNodeIds: Array<number>,
    readonly members: Array<RouteMemberInfo>,
    readonly expectedName: string,
    readonly map: RouteMap,
    readonly structureStrings: Array<string>,
    readonly geometryDigest: string,
    readonly locationAnalysis: RouteLocationAnalysis
  ) {}

  public static fromJSON(jsonObject: any): RouteInfoAnalysis {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteInfoAnalysis(
      jsonObject.unexpectedNodeIds,
      jsonObject.members.map((json: any) => RouteMemberInfo.fromJSON(json)),
      jsonObject.expectedName,
      RouteMap.fromJSON(jsonObject.map),
      jsonObject.structureStrings,
      jsonObject.geometryDigest,
      RouteLocationAnalysis.fromJSON(jsonObject.locationAnalysis)
    );
  }
}
