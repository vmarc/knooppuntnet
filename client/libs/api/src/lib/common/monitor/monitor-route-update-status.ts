// this file is generated, please do not modify

import { MonitorRouteUpdateStep } from './monitor-route-update-step';

export interface MonitorRouteUpdateStatus {
  readonly steps: MonitorRouteUpdateStep[];
  readonly done: boolean;
  readonly errors: string[];
  readonly exception: string;
}
