import { SegmentInfo } from '@api/common/route/segment-info';

export interface SegmentPopupEvent {
  readonly event: MouseEvent;
  readonly segment: SegmentInfo;
}
