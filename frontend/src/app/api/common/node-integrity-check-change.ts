// this file is generated, please do not modify

import { NodeIntegrityCheck } from './node-integrity-check';
import { RouteType } from './route-type';

export interface NodeIntegrityCheckChange {
  readonly routeType: RouteType;
  readonly before?: NodeIntegrityCheck;
  readonly after?: NodeIntegrityCheck;
}
