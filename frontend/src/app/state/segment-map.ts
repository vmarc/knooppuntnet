import { SegmentInfo } from '@api/common/route/segment-info';
import { SegmentColors } from '@app/mapold/domain/segment-colors';

export class SegmentMap {
  constructor(private segmentMap: Map<string, number>) {}

  static from(segments: ReadonlyArray<SegmentInfo>): SegmentMap {
    const map: Map<string, number> = new Map();
    segments.forEach((segment) => {
      segment.routeInfos.forEach((routeInfo) => {
        routeInfo.segmentIds.forEach((segmentId) => {
          const key = `${routeInfo.relationId}-${segmentId}`;
          map.set(key, segment.id);
        });
      });
    });
    return new SegmentMap(map);
  }

  color(routeId: string, segmentId: string): string | undefined {
    const key = `${routeId}-${segmentId}`;
    const superSegmentId = this.segmentMap.get(key);
    if (superSegmentId) {
      return SegmentColors.colorForSegmentId(superSegmentId);
    }
    return undefined;
  }
}
