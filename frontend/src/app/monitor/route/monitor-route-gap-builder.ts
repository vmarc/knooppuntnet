import { ElementRef } from '@angular/core';

export class MonitorRouteGapBuilder {
  private readonly parts: string[];
  private readonly width: number;
  private readonly height: number;
  private readonly lineWidth = 1;
  private readonly rectangleSize = 4;
  private readonly gapSize = 4;
  private readonly nodeRadius = 3;
  private readonly yTop: number;
  private readonly yBottom: number;
  private readonly yMiddle1: number;
  private readonly yMiddle2: number;

  private context: CanvasRenderingContext2D;

  constructor(canvas: ElementRef<HTMLCanvasElement>, description: string) {
    this.context = canvas.nativeElement.getContext('2d');
    this.width = canvas.nativeElement.width;
    this.height = canvas.nativeElement.height;
    this.parts = description.split('-');

    this.yTop = this.gapSize / 2 + this.rectangleSize / 2;
    this.yBottom = this.height - this.gapSize / 2 - this.rectangleSize / 2;
    this.yMiddle1 = this.height / 2 - this.gapSize / 2 - this.rectangleSize / 2;
    this.yMiddle2 = this.height / 2 + this.gapSize / 2 + this.rectangleSize / 2;
  }

  draw(): void {
    this.drawLines();

    if (this.parts.includes('start')) {
      this.drawNode(this.nodeRadius);
    }

    if (this.parts.includes('end')) {
      this.drawNode(this.height - this.nodeRadius);
    }

    if (this.parts.includes('top')) {
      this.drawConnector(this.yTop);
    }

    if (this.parts.includes('bottom')) {
      this.drawConnector(this.yBottom);
    }

    if (this.parts.includes('middle')) {
      this.drawConnector(this.yMiddle1);
      this.drawConnector(this.yMiddle2);
    }
  }

  private drawLines(): void {
    this.context.lineWidth = this.lineWidth;
    this.context.strokeStyle = 'black';

    if (this.parts.includes('top')) {
      if (this.parts.includes('middle')) {
        this.drawLine(this.gapSize, this.yMiddle1);
        if (this.parts.includes('bottom')) {
          this.drawLine(this.yMiddle2, this.yBottom);
        } else {
          this.drawLine(this.yMiddle2, this.height);
        }
      } else {
        if (this.parts.includes('bottom')) {
          this.drawLine(this.gapSize, this.yBottom);
        } else {
          this.drawLine(this.gapSize, this.height);
        }
      }
    } else if (this.parts.includes('middle')) {
      this.drawLine(0, this.yMiddle1);
      if (this.parts.includes('bottom')) {
        this.drawLine(this.yMiddle2, this.yBottom);
      } else {
        this.drawLine(this.yMiddle2, this.height);
      }
    } else if (this.parts.includes('bottom')) {
      this.drawLine(0, this.yBottom);
    } else {
      this.drawLine(0, this.height);
    }
  }

  private drawConnector(y: number) {
    this.context.beginPath();
    this.context.fillStyle = 'red';
    this.context.rect(
      this.width / 2 - this.rectangleSize / 2,
      y - this.rectangleSize / 2,
      this.rectangleSize,
      this.rectangleSize
    );
    this.context.fill();
    this.context.closePath();
  }

  private drawLine(top: number, bottom: number): void {
    this.context.beginPath();
    this.context.moveTo(this.width / 2, top);
    this.context.lineTo(this.width / 2, bottom);
    this.context.stroke();
    this.context.closePath();
  }

  private drawNode(y: number): void {
    this.context.beginPath();
    this.context.fillStyle = 'blue';
    this.context.arc(this.width / 2, y, this.nodeRadius, 0, Math.PI * 2);
    this.context.fill();
    this.context.closePath();
  }
}
