import { ElementRef } from '@angular/core';
import { LinkInfo } from '@api/common/route/link-info';

export class TryoutLinkBuilder {
  private readonly width: number;
  private readonly height: number;
  private readonly lineWidth = 1;
  private context: CanvasRenderingContext2D;

  constructor(
    canvas: ElementRef<HTMLCanvasElement>,
    private linkInfo: LinkInfo
  ) {
    this.context = canvas.nativeElement.getContext('2d');
    this.height = canvas.nativeElement.height;
    this.width = canvas.nativeElement.width;
  }

  draw(): void {
    this.context.fillStyle = 'lightgray';
    this.context.fillRect(0, 0, 20, 20);

    this.context.lineWidth = this.lineWidth;
    this.context.strokeStyle = 'red';
    this.context.beginPath();
    this.context.moveTo(0, 0);
    this.context.lineTo(this.width, this.height);
    this.context.stroke();
    this.context.closePath();

    this.context.strokeStyle = 'blue';
    this.context.beginPath();
    this.context.moveTo(0, this.height);
    this.context.lineTo(this.width, 0);
    this.context.stroke();
    this.context.closePath();

    this.context.strokeStyle = 'green';
    this.context.beginPath();
    this.context.moveTo(0, 100);
    this.context.lineTo(200, 100);
    this.context.stroke();
    this.context.closePath();
  }
}
