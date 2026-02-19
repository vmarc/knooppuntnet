// this file is generated, please do not modify

import { MonitorRouteUpdateStep } from './monitor-route-update-step';

export interface MonitorRouteUpdateStatus {
  readonly steps: ReadonlyArray<MonitorRouteUpdateStep>;
  readonly done: boolean;
  readonly errors: ReadonlyArray<string>;
  readonly exception?: string;
}
