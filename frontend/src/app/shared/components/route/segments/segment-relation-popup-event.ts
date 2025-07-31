import { SegmentInfo } from '@api/common/route/segment-info';

export interface SegmentRelationPopupEvent {
  readonly event: MouseEvent;
  readonly segment: SegmentInfo;
  readonly relationId: number;
}
