import { Link } from '@api/common/route/link';

export class TryoutLinkBuilder {
  private readonly width = 40;
  private context: CanvasRenderingContext2D;

  constructor(
    canvas: HTMLCanvasElement,
    private height: number,
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

  drawNode(): void {
    const x = this.width / 2;
    const y = this.height / 2;
    this.context.fillStyle = 'blue';
    this.context.beginPath();
    this.context.arc(x, y, 3, 0, Math.PI * 2);
    this.context.fill();
    this.context.closePath();
  }

  draw(): void {
    const ymax = this.height - 1;
    const xloop = 14;

    let xowloop = 0;
    if (this.link.isOnewayLoopBackwardPart) {
      xowloop = 7;
    } else if (this.link.isOnewayLoopForwardPart) {
      xowloop = -7;
    }

    let xoff: number;
    if (this.link.isLoop) {
      xoff = this.width / 2 - (xloop / 2 - 1);
    } else {
      xoff = this.width / 2;
    }

    const w = 4;
    const p = 4 + w + 1;
    let y1 = 0;
    let y2 = 0;

    if (!this.link.hasPrev) {
      if (this.link.isLoop) {
        this.context.strokeStyle = 'black';
        y1 = 5;
        // left arc
        this.context.beginPath();
        this.context.arc(xoff + 3, y1, 3, -Math.PI, -Math.PI / 2);
        this.context.stroke();
        this.context.closePath();
        // right arc
        this.context.beginPath();
        this.context.arc(xoff + xloop - 3, y1, 3, -Math.PI / 2, 0);
        this.context.stroke();
        this.context.closePath();
        this.drawLine(xoff + 3, y1 - 3, xoff + xloop - 3, y1 - 3);
      } else {
        this.context.strokeStyle = 'red';
        this.context.fillStyle = 'red';
        if (this.link.isOnewayHead) {
          this.drawRect(xoff - 2, p - 3 - w, w, w);
          this.fillRect(xoff - 2, p - 3 - w, w, w);
        } else {
          this.drawRect(xoff - 2 + xowloop, p - 1 - w, w, w);
          this.fillRect(xoff - 2 + xowloop, p - 1 - w, w, w);
        }
        y1 = p;
      }
    }

    if (this.link.hasNext) {
      y2 = ymax;
    } else {
      if (this.link.isLoop) {
        this.context.strokeStyle = 'black';
        this.context.fillStyle = 'black';
        y2 = ymax - 5;
        this.fillRect(xoff - 1, y2 + 2, 3, 3);
        this.drawLine(xoff, y2, xoff, y2 + 2);
        // right arc
        this.context.beginPath();
        this.context.arc(xoff + xloop - 3, y2, 3, 0, Math.PI / 2);
        this.context.stroke();
        this.context.closePath();

        this.drawLine(xoff + 3 - 1, y2 + 3, xoff + xloop - 3, y2 + 3);
      } else {
        this.context.strokeStyle = 'red';
        this.context.fillStyle = 'red';
        if (this.link.isOnewayTail) {
          this.drawRect(xoff - 2, ymax - p + 3, w, w);
          this.fillRect(xoff - 2, ymax - p + 3, w, w);
        } else {
          this.drawRect(xoff - 2 + xowloop, ymax - p + 1, w, w);
          this.fillRect(xoff - 2 + xowloop, ymax - p + 1, w, w);
        }
        y2 = ymax - p;
      }
    }

    // vertical lines
    this.context.strokeStyle = 'blue';

    if (this.link.isLoop) {
      this.drawLine(xoff + xloop, y1, xoff + xloop, y2);
    }

    if (this.link.isOnewayHead) {
      this.context.setLineDash([2, 2]);
      y1 = 7;
      this.context.beginPath();
      this.context.moveTo(xoff - xowloop + 1, ymax);
      this.context.lineTo(xoff - xowloop + 1, y1 + 1);
      this.context.lineTo(xoff, 1);
      this.context.stroke();
      this.context.closePath();

      this.context.setLineDash([]);
      this.drawLine(xoff + xowloop, y1 + 1, xoff, 1);
    }

    if (this.link.isOnewayTail) {
      this.context.setLineDash([2, 2]);
      y2 = ymax - 7;
      this.context.beginPath();
      this.context.moveTo(xoff + 1, ymax - 1);
      this.context.lineTo(xoff - xowloop + 1, y2);
      this.context.lineTo(xoff - xowloop + 1, y1);
      this.context.stroke();
      this.context.closePath();
      this.context.setLineDash([]);
      this.drawLine(xoff + xowloop, y2, xoff, ymax - 1);
    }

    if (
      (this.link.isOnewayLoopForwardPart || this.link.isOnewayLoopBackwardPart) &&
      !this.link.isOnewayTail &&
      !this.link.isOnewayHead
    ) {
      this.context.setLineDash([2, 2]);
      this.drawLine(xoff - xowloop + 1, y1, xoff - xowloop + 1, y2 + 1);
      this.context.setLineDash([]);
    }

    if (!this.link.isOnewayLoopForwardPart && !this.link.isOnewayLoopBackwardPart) {
      this.drawLine(xoff, y1, xoff, y2);
    }

    this.drawLine(xoff + xowloop, y1, xoff + xowloop, y2);

    // special icons
    this.drawRoundabout(xoff, this.height / 2);
    this.drawArrow(xoff, xoff + xowloop, (y1 + y2) / 2 - 2);
  }

  private drawRoundabout(x: number, y: number): void {
    const direction = this.link.direction;
    if (direction === 'roundabout-left' || direction === 'roundabout-right') {
      this.context.fillStyle = 'white';
      this.context.strokeStyle = 'blue';
      this.context.beginPath();
      this.context.arc(x, y, 9, 0, Math.PI * 2);
      this.context.fill();
      this.context.stroke();
      this.context.closePath();

      this.context.strokeStyle = 'blue';
      this.context.beginPath();
      this.context.arc(x, y, 3, 0, Math.PI * 2);
      this.context.stroke();
      this.context.closePath();
    }
  }

  private drawArrow(xLeft: number, xRight: number, y: number): void {
    const direction = this.link.direction;
    if (direction === 'forward' || direction === 'backward') {
      if (!this.link.isOnewayLoopForwardPart && !this.link.isOnewayLoopBackwardPart) {
        this.drawArrow1(xLeft, y, direction === 'forward');
      }
      if (this.link.isOnewayLoopBackwardPart && this.link.isOnewayLoopForwardPart) {
        this.drawArrow1(xRight, y, direction === 'backward');
      } else {
        this.drawArrow1(xRight, y, direction === 'forward');
      }
    }
  }

  private drawArrow1(x: number, y: number, down: boolean): void {
    const xLeft = x - 3;
    const xRight = x + 3;
    const height = 7;
    const yArrowPoint = down ? y + height : y;
    const yArrowStart = down ? y : y + height;

    this.drawLine(xLeft, yArrowStart, x, yArrowPoint);
    this.drawLine(xRight, yArrowStart, x, yArrowPoint);
  }

  private drawLine(x1: number, y1: number, x2: number, y2: number): void {
    this.context.beginPath();
    this.context.moveTo(x1, y1);
    this.context.lineTo(x2, y2);
    this.context.stroke();
    this.context.closePath();
  }

  private drawRect(x1: number, y1: number, x2: number, y2: number): void {
    this.context.strokeRect(x1, y1, x2, y2);
  }

  private fillRect(x1: number, y1: number, x2: number, y2: number): void {
    this.context.fillRect(x1, y1, x2, y2);
  }
}
