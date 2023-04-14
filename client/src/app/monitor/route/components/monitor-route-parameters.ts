import { MonitorRouteProperties } from '@api/common/monitor';

export interface MonitorRouteParameters {
  mode: string;
  initialProperties: MonitorRouteProperties;
  properties: MonitorRouteProperties;
  referenceFile: File | null;
}
