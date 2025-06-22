import { SegmentColors } from '@app/map/domain/segment-colors';

export class DrawSegment {
  constructor(
    private context: CanvasRenderingContext2D,
    private height: number,
    private segmentIds: number[]
  ) {}

  draw(): void {
    if (this.segmentIds.length === 1) {
      const segmentId = this.segmentIds[0];
      this.context.strokeStyle = SegmentColors.colorForSegmentId(segmentId);
      this.drawLine(0, 0, 0, this.height);
    }
  }

  private drawLine(x1: number, y1: number, x2: number, y2: number): void {
    this.context.beginPath();
    this.context.moveTo(x1, y1);
    this.context.lineTo(x2, y2);
    this.context.stroke();
    this.context.closePath();
  }
}
