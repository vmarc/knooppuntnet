import { MemberType } from '@api/common/data/member-type';
import { Link } from '@api/common/route/link';

export class DrawLink {
  private readonly width = 40;

  private static readonly STUB_SIZE = 4;
  private static readonly STUB_PADDING = 4 + DrawLink.STUB_SIZE + 1;

  constructor(
    private context: CanvasRenderingContext2D,
    private height: number,
    private memberType: MemberType,
    private link: Link
  ) {}

  draw(): void {
    this.context.fillStyle = 'blue';
    this.context.strokeStyle = 'blue';
    if (this.memberType === 'node') {
      this.drawNode();
    } else if (this.memberType === 'way') {
      this.drawWay();
    } else {
      this.drawRelation();
    }
  }

  private drawNode(): void {
    const centerX = this.width / 2;
    const centerY = this.height / 2;
    this.fillArc(centerX, centerY, 3, 0, Math.PI * 2);
  }

  private drawRelation(): void {
    const centerX = this.width / 2;
    const centerY = this.height / 2;

    this.context.beginPath();
    // Vertical line
    this.context.moveTo(centerX, centerY - 5);
    this.context.lineTo(centerX, centerY + 5);
    // Horizontal line
    this.context.moveTo(centerX - 5, centerY);
    this.context.lineTo(centerX + 5, centerY);
    this.context.stroke();
    this.context.closePath();
  }

  private drawWay(): void {
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

    let y1 = 0;
    let y2 = 0;

    if (!this.link.hasPrev) {
      if (this.link.isLoop) {
        y1 = 5;
        this.strokeArc(xoff + 3, y1, 3, -Math.PI, -Math.PI / 2); // left arc
        this.strokeArc(xoff + xloop - 3, y1, 3, -Math.PI / 2, 0); // right arc
        this.drawLine(xoff + 3, y1 - 3, xoff + xloop - 3, y1 - 3);
      } else {
        this.context.strokeStyle = 'red';
        this.context.fillStyle = 'red';
        if (this.link.isOnewayHead) {
          this.fillRect(
            xoff - 2,
            DrawLink.STUB_PADDING - 3 - DrawLink.STUB_SIZE,
            DrawLink.STUB_SIZE,
            DrawLink.STUB_SIZE
          );
        } else {
          this.fillRect(
            xoff - 2 + xowloop,
            DrawLink.STUB_PADDING - 1 - DrawLink.STUB_SIZE,
            DrawLink.STUB_SIZE,
            DrawLink.STUB_SIZE
          );
        }
        this.context.strokeStyle = 'blue';
        this.context.fillStyle = 'blue';
        y1 = DrawLink.STUB_PADDING;
      }
    }

    if (this.link.hasNext) {
      y2 = ymax;
    } else {
      if (this.link.isLoop) {
        y2 = ymax - 5;
        this.fillRect(xoff - 1, y2 + 2, 3, 3);
        this.drawLine(xoff, y2, xoff, y2 + 2);
        this.strokeArc(xoff + xloop - 3, y2, 3, 0, Math.PI / 2); // right arc
        this.drawLine(xoff + 3 - 1, y2 + 3, xoff + xloop - 3, y2 + 3);
      } else {
        this.context.strokeStyle = 'red';
        this.context.fillStyle = 'red';
        if (this.link.isOnewayTail) {
          this.fillRect(
            xoff - 2,
            ymax - DrawLink.STUB_PADDING + 3,
            DrawLink.STUB_SIZE,
            DrawLink.STUB_SIZE
          );
        } else {
          this.fillRect(
            xoff - 2 + xowloop,
            ymax - DrawLink.STUB_PADDING + 1,
            DrawLink.STUB_SIZE,
            DrawLink.STUB_SIZE
          );
        }
        this.context.strokeStyle = 'blue';
        this.context.fillStyle = 'blue';
        y2 = ymax - DrawLink.STUB_PADDING;
      }
    }

    // vertical lines
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
      // Outer circle
      this.context.fillStyle = 'white';
      this.fillArc(x, y, 9, 0, Math.PI * 2);
      this.context.strokeStyle = 'blue';
      this.strokeArc(x, y, 9, 0, Math.PI * 2);

      // Inner circle
      this.context.strokeStyle = 'blue';
      this.strokeArc(x, y, 3, 0, Math.PI * 2);
    }
  }

  private drawArrow(xLeft: number, xRight: number, y: number): void {
    const direction = this.link.direction;
    if (direction === 'forward' || direction === 'backward') {
      if (!this.link.isOnewayLoopForwardPart && !this.link.isOnewayLoopBackwardPart) {
        this.drawArrowShape(xLeft, y, direction === 'forward');
      }
      if (this.link.isOnewayLoopBackwardPart && this.link.isOnewayLoopForwardPart) {
        this.drawArrowShape(xRight, y, direction === 'backward');
      } else {
        this.drawArrowShape(xRight, y, direction === 'forward');
      }
    }
  }

  private drawArrowShape(x: number, y: number, down: boolean): void {
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

  private fillRect(x1: number, y1: number, x2: number, y2: number): void {
    this.context.strokeRect(x1, y1, x2, y2);
    this.context.fillRect(x1, y1, x2, y2);
  }

  private strokeArc(
    x: number,
    y: number,
    radius: number,
    startAngle: number,
    endAngle: number
  ): void {
    this.context.beginPath();
    this.context.arc(x, y, radius, startAngle, endAngle);
    this.context.stroke();
    this.context.closePath();
  }
  private fillArc(
    x: number,
    y: number,
    radius: number,
    startAngle: number,
    endAngle: number
  ): void {
    this.context.beginPath();
    this.context.arc(x, y, radius, startAngle, endAngle);
    this.context.fill();
    this.context.closePath();
  }
}
