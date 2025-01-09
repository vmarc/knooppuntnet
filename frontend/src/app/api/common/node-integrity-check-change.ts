// this file is generated, please do not modify

import { RouteType } from './route-type';
import { NodeIntegrityCheck } from './node-integrity-check';

export interface NodeIntegrityCheckChange {
  readonly routeType: RouteType;
  readonly before?: NodeIntegrityCheck;
  readonly after?: NodeIntegrityCheck;
}
