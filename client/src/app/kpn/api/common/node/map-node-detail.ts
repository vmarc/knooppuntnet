// this file is generated, please do not modify

import { Reference } from '../common/reference';
import { Timestamp } from '@api/custom/timestamp';

export interface MapNodeDetail {
  readonly id: number;
  readonly name: string;
  readonly latitude: string;
  readonly longitude: string;
  readonly lastUpdated: Timestamp;
  readonly networkReferences: Reference[];
  readonly routeReferences: Reference[];
}
