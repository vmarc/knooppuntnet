// this class is generated, please do not modify

import { TrackSegment } from './track-segment';

export class TrackPath {
  constructor(
    readonly pathId: number,
    readonly startNodeId: number,
    readonly endNodeId: number,
    readonly meters: number,
    readonly oneWay: boolean,
    readonly segments: Array<TrackSegment>
  ) {}

  static fromJSON(jsonObject: any): TrackPath {
    if (!jsonObject) {
      return undefined;
    }
    return new TrackPath(
      jsonObject.pathId,
      jsonObject.startNodeId,
      jsonObject.endNodeId,
      jsonObject.meters,
      jsonObject.oneWay,
      jsonObject.segments.map((json: any) => TrackSegment.fromJSON(json))
    );
  }
}
