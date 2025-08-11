// this file is generated, please do not modify

import { Timestamp } from '@api/custom/timestamp';

export interface MonitorRouteGpxPage {
  readonly groupName: string;
  readonly routeName: string;
  readonly subRelationId: number;
  readonly subRelationDescription: string;
  readonly referenceTimestamp: Timestamp;
  readonly referenceFilename?: string;
  readonly referenceDistance: number;
}
