export class SymbolShape {
  private static outlineLineWidth = 0.02;
  private static outlineStrokeStyle = 'lightgrey';

  private static readonly backgroundShapeFunctions = new Map<
    string,
    (context: CanvasRenderingContext2D) => void
  >([
    ['circle', this.drawBackgroundCircle],
    ['frame', this.drawBackgroundFrame],
    ['round', this.drawBackgroundRound],
  ]);

  private static readonly foregroundShapeFunctions = new Map<
    string,
    (context: CanvasRenderingContext2D) => void
  >([
    ['arch', this.drawArch],
    ['arrow', this.drawArrow],
    ['backslash', this.drawBackslash],
    ['bar', this.drawBar],
    ['bowl', this.drawBowl],
    ['circle', this.drawCircle],
    ['corner', this.drawCorner],
    ['corner_left', this.drawCornerLeft],
    ['crest', this.drawCrest],
    ['cross', this.drawCross],
    ['diamond_line', this.drawDiamondLine],
    ['diamond', this.drawDiamond],
    ['diamond_left', this.drawDiamondLeft],
    ['diamond_right', this.drawDiamondRight],
    ['dot', this.drawDot],
    ['drop', this.drawDrop],
    ['drop_line', this.drawDropLine],
    ['fork', this.drawFork],
    ['house', this.drawHouse],
    ['L', this.drawL],
    ['left', this.drawLeft],
    ['left_arrow', this.drawLeftArrow],
    ['left_pointer', this.drawLeftPointer],
    ['lower', this.drawLower],
    ['right', this.drawRight],
    ['pointer', this.drawPointer],
    ['rectangle_line', this.drawRectangleLine],
    ['rectangle', this.drawRectangle],
    ['right_arrow', this.drawRightArrow],
    ['right_pointer', this.drawRightPointer],
    ['slash', this.drawSlash],
    ['stripe', this.drawStripe],
    ['triangle_line', this.drawTriangleLine],
    ['triangle', this.drawTriangle],
    ['triangle_turned', this.drawTriangleTurned],
    ['turned_T', this.drawTurnedT],
    ['upper', this.drawUpper],
    ['upper_bowl', this.drawUpperBowl],
    ['x', this.drawX],
    ['hexagon', this.drawHexagon],
    ['shell', this.drawShell],
    ['shell_modern', this.drawShellModern],
    ['hiker', this.drawHiker],
    ['wheel', this.drawWheel],
  ]);

  static readonly backgroundShapes = Array.from(
    this.backgroundShapeFunctions.keys()
  );
  static readonly foregroundShapes = Array.from(
    this.foregroundShapeFunctions.keys()
  );

  static drawBackground(
    context: CanvasRenderingContext2D,
    shape: string
  ): void {
    const drawFunction = this.backgroundShapeFunctions.get(shape);
    if (!drawFunction) {
      console.error(
        `unknown symbol background shape '${shape}' (no drawing function available for this shape)`
      );
    } else {
      drawFunction(context);
    }
  }

  static drawForeground(
    context: CanvasRenderingContext2D,
    shape: string
  ): void {
    const drawFunction = this.foregroundShapeFunctions.get(shape);
    if (!drawFunction) {
      console.error(
        `unknown symbol foreground shape '${shape}' (no drawing function available for this shape)`
      );
    } else {
      drawFunction(context);
    }
  }

  private static drawBackgroundCircle(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.1;
    context.arc(0.5, 0.5, 0.4, 0, 2 * Math.PI);
    context.stroke();
  }

  private static drawBackgroundFrame(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.1;
    context.rect(0.15, 0.15, 0.7, 0.7);
    context.stroke();
  }

  private static drawBackgroundRound(context: CanvasRenderingContext2D): void {
    context.arc(0.5, 0.5, 0.4, 0, 2 * Math.PI);
    context.fill();
  }

  private static drawArch(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.22;
    context.moveTo(0.25, 0.9);
    context.arc(0.5, 0.5, 0.25, Math.PI, 0);
    context.lineTo(0.75, 0.9);
    context.stroke();
  }

  private static drawArrow(context: CanvasRenderingContext2D): void {
    context.moveTo(0.1, 0.7);
    context.lineTo(0.55, 0.7);
    context.lineTo(0.55, 0.95);
    context.lineTo(0.95, 0.5);
    context.lineTo(0.55, 0.05);
    context.lineTo(0.55, 0.3);
    context.lineTo(0.1, 0.3);
    context.lineTo(0.1, 0.7);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawBackslash(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.25;
    context.moveTo(0, 0);
    context.lineTo(1, 1);
    context.stroke();
  }

  private static drawBar(context: CanvasRenderingContext2D): void {
    context.rect(0, 0.33, 1, 0.33);
    context.fill();
    context.closePath();
    context.beginPath();
    context.lineWidth = SymbolShape.outlineLineWidth;
    context.strokeStyle = SymbolShape.outlineStrokeStyle;
    context.moveTo(0, 0.33);
    context.lineTo(1, 0.33);
    context.moveTo(0, 0.66);
    context.lineTo(1, 0.66);
    context.stroke();
  }

  private static drawBowl(context: CanvasRenderingContext2D): void {
    context.moveTo(0.05, 0.5);
    context.lineTo(0.95, 0.5);
    context.arc(0.5, 0.5, 0.45, 0, Math.PI);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawCircle(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.12;
    context.arc(0.5, 0.5, 0.33, 0, 2 * Math.PI);
    context.stroke();
  }

  private static drawCorner(context: CanvasRenderingContext2D): void {
    context.moveTo(0, 0);
    context.lineTo(1, 1);
    context.lineTo(1, 0);
    context.closePath();
    context.fill();
    context.closePath();
    context.beginPath();
    context.lineWidth = SymbolShape.outlineLineWidth;
    context.strokeStyle = SymbolShape.outlineStrokeStyle;
    context.moveTo(0, 0);
    context.lineTo(1, 1);
    context.stroke();
  }

  private static drawCornerLeft(context: CanvasRenderingContext2D): void {
    context.moveTo(1, 0);
    context.lineTo(0, 1);
    context.lineTo(0, 0);
    context.closePath();
    context.fill();
    context.closePath();
    context.beginPath();
    context.lineWidth = SymbolShape.outlineLineWidth;
    context.strokeStyle = SymbolShape.outlineStrokeStyle;
    context.moveTo(1, 0);
    context.lineTo(0, 1);
    context.stroke();
  }

  private static drawCrest(context: CanvasRenderingContext2D): void {
    context.moveTo(0.15, 0.5);
    context.lineTo(0.15, 0.05);
    context.lineTo(0.85, 0.05);
    context.lineTo(0.85, 0.5);
    context.bezierCurveTo(0.85, 0.728, 0.701, 0.921, 0.5, 0.95);
    context.bezierCurveTo(0.299, 0.921, 0.148, 0.728, 0.15, 0.5);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawCross(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.25;
    context.moveTo(0, 0.5);
    context.lineTo(1, 0.5);
    context.stroke();
    context.moveTo(0.5, 0);
    context.lineTo(0.5, 1);
    context.stroke();
  }

  private static drawDiamondLine(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.1;
    context.moveTo(0, 0.5);
    context.lineTo(0.5, 0.25);
    context.lineTo(1, 0.5);
    context.lineTo(0.5, 0.75);
    context.lineTo(0, 0.5);
    context.stroke();
  }

  private static drawDiamond(context: CanvasRenderingContext2D): void {
    context.moveTo(0, 0.5);
    context.lineTo(0.5, 0.25);
    context.lineTo(1, 0.5);
    context.lineTo(0.5, 0.75);
    context.lineTo(0, 0.5);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawDiamondLeft(context: CanvasRenderingContext2D): void {
    context.moveTo(0, 0.5);
    context.lineTo(0.5, 0.25);
    context.lineTo(0.5, 0.75);
    context.lineTo(0, 0.5);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawDiamondRight(context: CanvasRenderingContext2D): void {
    context.lineTo(0.5, 0.25);
    context.lineTo(1, 0.5);
    context.lineTo(0.5, 0.75);
    context.lineTo(0.5, 0.25);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawDot(context: CanvasRenderingContext2D): void {
    context.arc(0.5, 0.5, 0.29, 0, 2 * Math.PI);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawDrop(context: CanvasRenderingContext2D): void {
    context.moveTo(0.5, 0.2);
    context.lineTo(0.9, 0.5);
    context.lineTo(0.5, 0.8);
    context.arc(0.4, 0.5, 0.3, 0.98, -0.98);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawDropLine(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.1;
    context.moveTo(0.5, 0.21);
    context.lineTo(0.9, 0.5);
    context.lineTo(0.5, 0.79);
    context.arc(0.4, 0.5, 0.3, 0.98, -0.98);
    context.stroke();
  }

  private static drawFork(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.15;
    context.moveTo(1, 0.5);
    context.lineTo(0.45, 0.5);
    context.lineTo(0, 0.1);
    context.stroke();
    context.moveTo(0.45, 0.5);
    context.lineTo(0, 0.9);
    context.stroke();
  }

  private static drawHouse(context: CanvasRenderingContext2D): void {
    context.moveTo(0.2, 0.9);
    context.lineTo(0.2, 0.4);
    context.lineTo(0.5, 0.1);
    context.lineTo(0.8, 0.4);
    context.lineTo(0.8, 0.9);
    context.lineTo(0.2, 0.9);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawL(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.3;
    context.moveTo(0.2, 0.05);
    context.lineTo(0.2, 0.8);
    context.lineTo(0.95, 0.8);
    context.stroke();
  }

  private static drawLeft(context: CanvasRenderingContext2D): void {
    context.rect(0, 0, 0.5, 1);
    context.fill();
    context.closePath();
    context.beginPath();
    context.lineWidth = SymbolShape.outlineLineWidth;
    context.strokeStyle = SymbolShape.outlineStrokeStyle;
    context.moveTo(0.5, 0);
    context.lineTo(0.5, 1);
    context.stroke();
  }

  private static drawLeftArrow(context: CanvasRenderingContext2D): void {
    context.moveTo(0.9, 0.7);
    context.lineTo(0.45, 0.7);
    context.lineTo(0.45, 0.95);
    context.lineTo(0.05, 0.5);
    context.lineTo(0.45, 0.05);
    context.lineTo(0.45, 0.3);
    context.lineTo(0.9, 0.3);
    context.lineTo(0.9, 0.7);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawLeftPointer(context: CanvasRenderingContext2D): void {
    context.moveTo(0.9, 0.1);
    context.lineTo(0.9, 0.9);
    context.lineTo(0.1, 0.5);
    context.lineTo(0.9, 0.1);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawLower(context: CanvasRenderingContext2D): void {
    context.rect(0, 0.5, 1, 0.5);
    context.fill();
    context.closePath();
    context.beginPath();
    context.lineWidth = SymbolShape.outlineLineWidth;
    context.strokeStyle = SymbolShape.outlineStrokeStyle;
    context.moveTo(0, 0.5);
    context.lineTo(1, 0.5);
    context.stroke();
  }

  private static drawRight(context: CanvasRenderingContext2D): void {
    context.rect(0.5, 0, 0.5, 1);
    context.fill();
    context.closePath();
    context.beginPath();
    context.lineWidth = SymbolShape.outlineLineWidth;
    context.strokeStyle = SymbolShape.outlineStrokeStyle;
    context.moveTo(0.5, 0);
    context.lineTo(0.5, 1);
    context.stroke();
  }

  private static drawPointer(context: CanvasRenderingContext2D): void {
    context.moveTo(0.1, 0.1);
    context.lineTo(0.1, 0.9);
    context.lineTo(0.9, 0.5);
    context.lineTo(0.1, 0.1);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawRectangleLine(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.15;
    context.rect(0.25, 0.25, 0.5, 0.5);
    context.stroke();
  }

  private static drawRectangle(context: CanvasRenderingContext2D): void {
    context.rect(0.25, 0.25, 0.5, 0.5);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawRightArrow(context: CanvasRenderingContext2D): void {
    context.moveTo(0.1, 0.7);
    context.lineTo(0.55, 0.7);
    context.lineTo(0.55, 0.95);
    context.lineTo(0.95, 0.5);
    context.lineTo(0.55, 0.05);
    context.lineTo(0.55, 0.3);
    context.lineTo(0.1, 0.3);
    context.lineTo(0.1, 0.7);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawRightPointer(context: CanvasRenderingContext2D): void {
    context.moveTo(0.1, 0.1);
    context.lineTo(0.1, 0.9);
    context.lineTo(0.9, 0.5);
    context.lineTo(0.1, 0.1);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawSlash(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.25;
    context.moveTo(1, 0);
    context.lineTo(0, 1);
    context.stroke();
  }

  private static drawStripe(context: CanvasRenderingContext2D): void {
    context.rect(0.33, 0, 0.33, 1);
    context.fill();
    context.closePath();
    context.beginPath();
    context.lineWidth = SymbolShape.outlineLineWidth;
    context.strokeStyle = SymbolShape.outlineStrokeStyle;
    context.moveTo(0.33, 0);
    context.lineTo(0.33, 1);
    context.moveTo(0.66, 0);
    context.lineTo(0.66, 1);
    context.stroke();
  }

  private static drawTriangleLine(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.15;
    context.moveTo(0.2, 0.8);
    context.lineTo(0.5, 0.2);
    context.lineTo(0.8, 0.8);
    context.closePath();
    context.stroke();
  }

  private static drawTriangle(context: CanvasRenderingContext2D): void {
    context.moveTo(0.2, 0.8);
    context.lineTo(0.5, 0.2);
    context.lineTo(0.8, 0.8);
    context.lineTo(0.2, 0.8);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawTriangleTurned(context: CanvasRenderingContext2D): void {
    context.moveTo(0.2, 0.2);
    context.lineTo(0.5, 0.8);
    context.lineTo(0.8, 0.2);
    context.lineTo(0.2, 0.2);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawTurnedT(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.2;
    context.moveTo(0.1, 0.8);
    context.lineTo(0.9, 0.8);
    context.moveTo(0.5, 0.2);
    context.lineTo(0.5, 0.8);
    context.stroke();
  }

  private static drawUpper(context: CanvasRenderingContext2D): void {
    context.rect(0, 0, 1, 0.5);
    context.fill();
    context.closePath();
    context.beginPath();
    context.lineWidth = SymbolShape.outlineLineWidth;
    context.strokeStyle = SymbolShape.outlineStrokeStyle;
    context.moveTo(0, 0.5);
    context.lineTo(1, 0.5);
    context.stroke();
  }

  static drawUpperBowl(context: CanvasRenderingContext2D): void {
    context.moveTo(0.05, 0.5);
    context.lineTo(0.95, 0.5);
    // context.arc_negative(0.5,0.5,0.45,0,Math.PI);
    context.arc(0.5, 0.5, 0.45, 0, -Math.PI);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawX(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.25;
    context.moveTo(1, 0);
    context.lineTo(0, 1);
    context.moveTo(0, 0);
    context.lineTo(1, 1);
    context.stroke();
  }

  private static drawHexagon(context: CanvasRenderingContext2D): void {
    context.moveTo(0.8, 0.5);
    context.lineTo(0.65, 0.24);
    context.lineTo(0.35, 0.24);
    context.lineTo(0.2, 0.5);
    context.lineTo(0.35, 0.76);
    context.lineTo(0.65, 0.76);
    context.lineTo(0.8, 0.5);
    context.fill();
    SymbolShape.strokeOutline(context);
  }

  private static drawShell(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.06;
    context.moveTo(0.5, 0.1);
    context.lineTo(0, 0.3);
    context.moveTo(0.5, 0.1);
    context.lineTo(0.1, 0.5);
    context.moveTo(0.5, 0.1);
    context.lineTo(0.2, 0.65);
    context.moveTo(0.5, 0.1);
    context.lineTo(0.35, 0.8);
    context.moveTo(0.5, 0.1);
    context.lineTo(0.5, 0.85);
    context.moveTo(0.5, 0.1);
    context.lineTo(0.65, 0.8);
    context.moveTo(0.5, 0.1);
    context.lineTo(0.8, 0.65);
    context.moveTo(0.5, 0.1);
    context.lineTo(0.9, 0.5);
    context.moveTo(0.5, 0.1);
    context.lineTo(1, 0.3);
    context.stroke();
  }

  private static drawShellModern(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.06;
    context.moveTo(0.1, 0.5);
    context.lineTo(0.3, 0);
    context.moveTo(0.1, 0.5);
    context.lineTo(0.5, 0.1);
    context.moveTo(0.1, 0.5);
    context.lineTo(0.65, 0.2);
    context.moveTo(0.1, 0.5);
    context.lineTo(0.8, 0.35);
    context.moveTo(0.1, 0.5);
    context.lineTo(0.85, 0.5);
    context.moveTo(0.1, 0.5);
    context.lineTo(0.8, 0.65);
    context.moveTo(0.1, 0.5);
    context.lineTo(0.65, 0.8);
    context.moveTo(0.1, 0.5);
    context.lineTo(0.5, 0.9);
    context.moveTo(0.1, 0.5);
    context.lineTo(0.3, 1);
    context.stroke();
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  private static drawHiker(context: CanvasRenderingContext2D): void {
    // leave empty
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  private static drawWheel(context: CanvasRenderingContext2D): void {
    // leave empty
  }

  private static strokeOutline(context: CanvasRenderingContext2D): void {
    context.lineWidth = SymbolShape.outlineLineWidth;
    context.strokeStyle = SymbolShape.outlineStrokeStyle;
    context.stroke();
  }
}
