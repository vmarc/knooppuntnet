// this class is generated, please do not modify

import { NetworkNodeDetail } from './network-node-detail';
import { NetworkSummary } from './network-summary';
import { NetworkType } from '../../custom/network-type';
import { SurveyDateInfo } from '../survey-date-info';
import { TimeInfo } from '../time-info';

export class NetworkNodesPage {
  constructor(
    readonly timeInfo: TimeInfo,
    readonly surveyDateInfo: SurveyDateInfo,
    readonly networkSummary: NetworkSummary,
    readonly networkType: NetworkType,
    readonly nodes: Array<NetworkNodeDetail>,
    readonly routeIds: Array<number>
  ) {}

  static fromJSON(jsonObject: any): NetworkNodesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkNodesPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      SurveyDateInfo.fromJSON(jsonObject.surveyDateInfo),
      NetworkSummary.fromJSON(jsonObject.networkSummary),
      jsonObject.networkType,
      jsonObject.nodes.map((json: any) => NetworkNodeDetail.fromJSON(json)),
      jsonObject.routeIds
    );
  }
}
