// this file is generated, please do not modify

import { TrackSegment } from './track-segment';

export interface TrackPath {
  readonly pathId: number;
  readonly startNodeId: number;
  readonly endNodeId: number;
  readonly meters: number;
  readonly oneWay: boolean;
  readonly segments: TrackSegment[];
}
