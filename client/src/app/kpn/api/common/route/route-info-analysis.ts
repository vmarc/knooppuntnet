// this class is generated, please do not modify

import {List} from 'immutable';
import {RouteMemberInfo} from '../../custom/route-member-info';
import {RouteLocationAnalysis} from '../route-location-analysis';
import {RouteMap} from './route-map';

export class RouteInfoAnalysis {

  constructor(readonly unexpectedNodeIds: List<number>,
              readonly members: List<RouteMemberInfo>,
              readonly expectedName: string,
              readonly map: RouteMap,
              readonly structureStrings: List<string>,
              readonly geometryDigest: string,
              readonly locationAnalysis: RouteLocationAnalysis) {
  }

  public static fromJSON(jsonObject: any): RouteInfoAnalysis {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteInfoAnalysis(
      jsonObject.unexpectedNodeIds ? List(jsonObject.unexpectedNodeIds) : List(),
      jsonObject.members ? List(jsonObject.members.map((json: any) => RouteMemberInfo.fromJSON(json))) : List(),
      jsonObject.expectedName,
      RouteMap.fromJSON(jsonObject.map),
      jsonObject.structureStrings ? List(jsonObject.structureStrings) : List(),
      jsonObject.geometryDigest,
      RouteLocationAnalysis.fromJSON(jsonObject.locationAnalysis)
    );
  }
}
