// this file is generated, please do not modify

import { ChangeKeyI } from '../changes/details/change-key-i';

export interface MonitorRouteChangeSummary {
  readonly key: ChangeKeyI;
  readonly groupName: string;
  readonly routeName: string;
  readonly groupDescription: string;
  readonly comment: string;
  readonly wayCount: number;
  readonly waysAdded: number;
  readonly waysRemoved: number;
  readonly waysUpdated: number;
  readonly osmDistance: number;
  readonly routeSegmentCount: number;
  readonly newNokSegmentCount: number;
  readonly resolvedNokSegmentCount: number;
  readonly happy: boolean;
  readonly investigate: boolean;
}
