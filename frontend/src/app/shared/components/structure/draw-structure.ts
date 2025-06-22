import { MemberType } from '@api/common/data/member-type';
import { Link } from '@api/common/route/link';
import { DrawSegment } from '@app/shared/components/structure/draw-segment';
import { DrawLink } from './draw-link';

export class DrawStructure {
  private readonly width = 80;
  private context: CanvasRenderingContext2D;

  constructor(
    canvas: HTMLCanvasElement,
    private height: number,
    private segmentIds: number[],
    private memberType: MemberType,
    private link: Link
  ) {
    const dpr = window.devicePixelRatio || 1;
    canvas.width = this.width * dpr;
    canvas.height = height * dpr;
    this.context = canvas.getContext('2d');
    this.context.scale(dpr, dpr);
    canvas.style.width = `${this.width}px`;
    canvas.style.height = `${this.height}px`;
  }

  draw(): void {
    this.context.translate(10, 0);
    new DrawSegment(this.context, this.height, this.segmentIds).draw();
    this.context.translate(30, 0);
    new DrawLink(this.context, this.height, this.memberType, this.link).draw();
  }
}
