// this file is generated, please do not modify

import { TrackPoint } from './track-point';
import { TrackSegmentFragment } from './track-segment-fragment';

export interface TrackSegment {
  readonly surface: string;
  readonly source: TrackPoint;
  readonly fragments: TrackSegmentFragment[];
}
