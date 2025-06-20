// this file is generated, please do not modify

import { LinkDirection } from './link-direction';

export interface Link {
  readonly memberIndex: number;
  readonly direction: LinkDirection;
  readonly hasPrev: boolean;
  readonly hasNext: boolean;
  readonly isLoop: boolean;
  readonly isOnewayLoopForwardPart: boolean;
  readonly isOnewayLoopBackwardPart: boolean;
  readonly isOnewayHead: boolean;
  readonly isOnewayTail: boolean;
}
