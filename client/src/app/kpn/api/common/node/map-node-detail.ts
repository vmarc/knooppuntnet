// this file is generated, please do not modify

import { Ref } from '../common/ref';
import { Timestamp } from '../../custom/timestamp';

export interface MapNodeDetail {
  readonly id: number;
  readonly name: string;
  readonly latitude: string;
  readonly longitude: string;
  readonly lastUpdated: Timestamp;
  readonly networkReferences: Ref[];
  readonly routeReferences: Ref[];
}
