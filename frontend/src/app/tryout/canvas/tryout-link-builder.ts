import { ElementRef } from '@angular/core';
import { LinkInfo } from '@api/common/route/link-info';

export class TryoutLinkBuilder {
  private readonly width = 40;
  private readonly lineWidth = 1;
  private context: CanvasRenderingContext2D;

  constructor(
    canvas: HTMLCanvasElement,
    private height: number,
    private linkInfo: LinkInfo
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
    this.context.fillStyle = 'lightgray';
    this.context.fillRect(0, 0, 20, 20);

    this.context.lineWidth = this.lineWidth;
    this.context.strokeStyle = 'red';
    this.context.beginPath();
    this.context.moveTo(0, 0);
    this.context.lineTo(this.width, this.height);
    this.context.stroke();
    this.context.closePath();

    this.context.strokeStyle = 'green';
    this.context.beginPath();
    this.context.moveTo(0, this.height);
    this.context.lineTo(this.width, 0);
    this.context.stroke();
    this.context.closePath();

    this.context.strokeStyle = 'blue';
    this.context.beginPath();
    this.context.moveTo(this.width / 2, 0);
    this.context.lineTo(this.width / 2, this.height);
    this.context.stroke();
    this.context.closePath();
  }
}
