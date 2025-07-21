// this file is generated, please do not modify

import { RouteDetails } from '@api/common/route/route-details';
import { Timestamp } from '@api/custom/timestamp';
import { MonitorReferenceType } from './monitor-reference-type';
import { MonitorRouteSummary } from './monitor-route-summary';

export interface MonitorRouteDetailsPage {
  readonly summary: MonitorRouteSummary;
  readonly comment: string;
  readonly symbol: string;
  readonly analysisTimestamp?: Timestamp;
  readonly analysisDuration: number;
  readonly referenceType: MonitorReferenceType;
  readonly referenceTimestamp?: Timestamp;
  readonly referenceFilename: string;
  readonly referenceDistance: number;
  readonly deviationDistance: number;
  readonly happy: boolean;
  readonly wayCount: number;
  readonly osmDistance: number;
  readonly relationCount: number;
  readonly relationLevels: number;
  readonly details: RouteDetails;
}
