// this class is generated, please do not modify

import {Fact} from '../../../custom/fact';
import {Ref} from '../../common/ref';
import {RawWay} from '../../data/raw/raw-way';
import {RouteData} from '../../diff/route-data';
import {RouteDiff} from '../../diff/route/route-diff';
import {WayUpdate} from '../../diff/way-update';
import {RouteLocationAnalysis} from '../../route-location-analysis';
import {ChangeKey} from './change-key';
import {ChangeType} from './change-type';

export class RouteChange {

  constructor(readonly key: ChangeKey,
              readonly changeType: ChangeType,
              readonly name: string,
              readonly locationAnalysis: RouteLocationAnalysis,
              readonly addedToNetwork: Array<Ref>,
              readonly removedFromNetwork: Array<Ref>,
              readonly before: RouteData,
              readonly after: RouteData,
              readonly removedWays: Array<RawWay>,
              readonly addedWays: Array<RawWay>,
              readonly updatedWays: Array<WayUpdate>,
              readonly diffs: RouteDiff,
              readonly facts: Array<Fact>,
              readonly happy: boolean,
              readonly investigate: boolean,
              readonly locationHappy: boolean,
              readonly locationInvestigate: boolean) {
  }

  static fromJSON(jsonObject: any): RouteChange {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteChange(
      ChangeKey.fromJSON(jsonObject.key),
      ChangeType.fromJSON(jsonObject.changeType),
      jsonObject.name,
      RouteLocationAnalysis.fromJSON(jsonObject.locationAnalysis),
      jsonObject.addedToNetwork.map((json: any) => Ref.fromJSON(json)),
      jsonObject.removedFromNetwork.map((json: any) => Ref.fromJSON(json)),
      RouteData.fromJSON(jsonObject.before),
      RouteData.fromJSON(jsonObject.after),
      jsonObject.removedWays.map((json: any) => RawWay.fromJSON(json)),
      jsonObject.addedWays.map((json: any) => RawWay.fromJSON(json)),
      jsonObject.updatedWays.map((json: any) => WayUpdate.fromJSON(json)),
      RouteDiff.fromJSON(jsonObject.diffs),
      jsonObject.facts,
      jsonObject.happy,
      jsonObject.investigate,
      jsonObject.locationHappy,
      jsonObject.locationInvestigate
    );
  }
}
