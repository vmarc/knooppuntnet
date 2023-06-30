export class SymbolShape {
  static readonly #drawFunctions = new Map<
    string,
    (context: CanvasRenderingContext2D) => void
  >([
    ['arch', this.#drawArch],
    ['arrow', this.#drawArrow],
    ['backslash', this.#drawBackslash],
    ['bar', this.#drawBar],
    ['bowl', this.#drawBowl],
    ['circle', this.#drawCircle],
    ['corner', this.#drawCorner],
    ['corner_left', this.#drawCornerLeft],
    ['crest', this.#drawCrest],
    ['cross', this.#drawCross],
    ['diamond_line', this.#drawDiamondLine],
    ['diamond', this.#drawDiamond],
    ['diamond_left', this.#drawDiamondLeft],
    ['diamond_right', this.#drawDiamondRight],
    ['dot', this.#drawDot],
    ['drop', this.#drawDrop],
    ['drop_line', this.#drawDropLine],
    ['fork', this.#drawFork],
    ['house', this.#drawHouse],
    ['L', this.#drawL],
    ['left', this.#drawLeft],
    ['left_arrow', this.#drawLeftArrow],
    ['left_pointer', this.#drawLeftPointer],
    ['lower', this.#drawLower],
    ['right', this.#drawRight],
    ['pointer', this.#drawPointer],
    ['rectangle_line', this.#drawRectangleLine],
    ['rectangle', this.#drawRectangle],
    ['right_arrow', this.#drawRightArrow],
    ['right_pointer', this.#drawRightPointer],
    ['slash', this.#drawSlash],
    ['stripe', this.#drawStripe],
    ['triangle_line', this.#drawTriangleLine],
    ['triangle', this.#drawTriangle],
    ['triangle_turned', this.#drawTriangleTurned],
    ['turned_T', this.#drawTurnedT],
    ['upper', this.#drawUpper],
    ['upper_bowl', this.#drawUpperBowl],
    ['x', this.#drawX],
    ['hexagon', this.#drawHexagon],
    ['shell', this.#drawShell],
    ['shell_modern', this.#drawShellModern],
  ]);

  static readonly #oldDrawFunctions: {
    [K: string]: (context: CanvasRenderingContext2D) => void;
  } = {
    arch: this.#drawArch,
    arrow: this.#drawArrow,
    backslash: this.#drawBackslash,
    bar: this.#drawBar,
    bowl: this.#drawBowl,
    circle: this.#drawCircle,
    corner: this.#drawCorner,
    corner_left: this.#drawCornerLeft,
    crest: this.#drawCrest,
    cross: this.#drawCross,
    diamond_line: this.#drawDiamondLine,
    diamond: this.#drawDiamond,
    diamond_left: this.#drawDiamondLeft,
    diamond_right: this.#drawDiamondRight,
    dot: this.#drawDot,
    drop: this.#drawDrop,
    drop_line: this.#drawDropLine,
    fork: this.#drawFork,
    house: this.#drawHouse,
    L: this.#drawL,
    left: this.#drawLeft,
    left_arrow: this.#drawLeftArrow,
    left_pointer: this.#drawLeftPointer,
    lower: this.#drawLower,
    right: this.#drawRight,
    pointer: this.#drawPointer,
    rectangle_line: this.#drawRectangleLine,
    rectangle: this.#drawRectangle,
    right_arrow: this.#drawRightArrow,
    right_pointer: this.#drawRightPointer,
    slash: this.#drawSlash,
    stripe: this.#drawStripe,
    triangle_line: this.#drawTriangleLine,
    triangle: this.#drawTriangle,
    triangle_turned: this.#drawTriangleTurned,
    turned_T: this.#drawTurnedT,
    upper: this.#drawUpper,
    upper_bowl: this.#drawUpperBowl,
    x: this.#drawX,
    hexagon: this.#drawHexagon,
    shell: this.#drawShell,
    shell_modern: this.#drawShellModern,
  };

  static shapes(): string[] {
    return Array.from(this.#drawFunctions.keys());
  }

  static draw(context: CanvasRenderingContext2D, shape: string): void {
    const drawFunction = this.#drawFunctions.get(shape);
    if (!drawFunction) {
      console.error(
        `unknown symbol shape '${shape}' (no drawing function available for this shape)`
      );
    } else {
      drawFunction(context);
    }
  }

  static #drawArch(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.22;
    context.moveTo(0.25, 0.9);
    context.arc(0.5, 0.5, 0.25, Math.PI, 0);
    context.lineTo(0.75, 0.9);
    context.stroke();
  }

  static #drawArrow(context: CanvasRenderingContext2D): void {
    context.moveTo(0.1, 0.7);
    context.lineTo(0.55, 0.7);
    context.lineTo(0.55, 0.95);
    context.lineTo(0.95, 0.5);
    context.lineTo(0.55, 0.05);
    context.lineTo(0.55, 0.3);
    context.lineTo(0.1, 0.3);
    context.lineTo(0.1, 0.7);
    context.fill();
  }

  static #drawBackslash(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.25;
    context.moveTo(0, 0);
    context.lineTo(1, 1);
    context.stroke();
  }

  static #drawBar(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.25;
    context.moveTo(0, 0.5);
    context.lineTo(1, 0.5);
    context.stroke();
  }

  static #drawBowl(context: CanvasRenderingContext2D): void {
    context.moveTo(0.05, 0.5);
    context.lineTo(0.95, 0.5);
    context.arc(0.5, 0.5, 0.45, 0, Math.PI);
    context.fill();
  }

  static #drawCircle(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.21;
    context.arc(0.5, 0.5, 0.33, 0, 2 * Math.PI);
    context.stroke();
  }

  static #drawCorner(context: CanvasRenderingContext2D): void {
    context.moveTo(0, 0);
    context.lineTo(1, 1);
    context.lineTo(1, 0);
    context.closePath();
    context.fill();
  }

  static #drawCornerLeft(context: CanvasRenderingContext2D): void {
    context.moveTo(1, 0);
    context.lineTo(0, 1);
    context.lineTo(0, 0);
    context.closePath();
    context.fill();
  }

  static #drawCrest(context: CanvasRenderingContext2D): void {
    context.moveTo(0.15, 0.5);
    context.lineTo(0.15, 0.05);
    context.lineTo(0.85, 0.05);
    context.lineTo(0.85, 0.5);
    context.bezierCurveTo(0.85, 0.728, 0.701, 0.921, 0.5, 0.95);
    context.bezierCurveTo(0.299, 0.921, 0.148, 0.728, 0.15, 0.5);
    context.fill();
  }

  static #drawCross(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.25;
    context.moveTo(0, 0.5);
    context.lineTo(1, 0.5);
    context.stroke();
    context.moveTo(0.5, 0);
    context.lineTo(0.5, 1);
    context.stroke();
  }

  static #drawDiamondLine(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.15;
    context.moveTo(0.1, 0.5);
    context.lineTo(0.5, 0.1);
    context.lineTo(0.9, 0.5);
    context.lineTo(0.5, 0.9);
    context.closePath();
    context.stroke();
  }

  static #drawDiamond(context: CanvasRenderingContext2D): void {
    context.moveTo(0, 0.5);
    context.lineTo(0.5, 0.25);
    context.lineTo(1, 0.5);
    context.lineTo(0.5, 0.75);
    context.fill();
  }

  static #drawDiamondLeft(context: CanvasRenderingContext2D): void {
    context.moveTo(0, 0.5);
    context.lineTo(0.5, 0.25);
    context.lineTo(0.5, 0.75);
    context.fill();
  }

  static #drawDiamondRight(context: CanvasRenderingContext2D): void {
    context.lineTo(0.5, 0.25);
    context.lineTo(1, 0.5);
    context.lineTo(0.5, 0.75);
    context.fill();
  }

  static #drawDot(context: CanvasRenderingContext2D): void {
    context.arc(0.5, 0.5, 0.29, 0, 2 * Math.PI);
    context.fill();
  }

  static #drawDrop(context: CanvasRenderingContext2D): void {
    context.moveTo(0.5, 0.2);
    context.lineTo(0.9, 0.5);
    context.lineTo(0.5, 0.8);
    context.arc(0.4, 0.5, 0.3, 0.98, -0.98);
    context.fill();
  }

  static #drawDropLine(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.1;
    context.moveTo(0.5, 0.21);
    context.lineTo(0.9, 0.5);
    context.lineTo(0.5, 0.79);
    context.arc(0.4, 0.5, 0.3, 0.98, -0.98);
    context.stroke();
  }

  static #drawFork(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.15;
    context.moveTo(1, 0.5);
    context.lineTo(0.45, 0.5);
    context.lineTo(0, 0.1);
    context.stroke();
    context.moveTo(0.45, 0.5);
    context.lineTo(0, 0.9);
    context.stroke();
  }

  static #drawHouse(context: CanvasRenderingContext2D): void {
    context.moveTo(0.2, 0.9);
    context.lineTo(0.2, 0.4);
    context.lineTo(0.5, 0.1);
    context.lineTo(0.8, 0.4);
    context.lineTo(0.8, 0.9);
    context.lineTo(0.2, 0.9);
    context.fill();
  }

  static #drawL(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.3;
    context.moveTo(0.2, 0.05);
    context.lineTo(0.2, 0.8);
    context.lineTo(0.95, 0.8);
    context.stroke();
  }

  static #drawLeft(context: CanvasRenderingContext2D): void {
    context.rect(0, 0, 0.5, 1);
    context.fill();
  }

  static #drawLeftArrow(context: CanvasRenderingContext2D): void {
    context.moveTo(0.9, 0.7);
    context.lineTo(0.45, 0.7);
    context.lineTo(0.45, 0.95);
    context.lineTo(0.05, 0.5);
    context.lineTo(0.45, 0.05);
    context.lineTo(0.45, 0.3);
    context.lineTo(0.9, 0.3);
    context.lineTo(0.9, 0.7);
    context.fill();
  }

  static #drawLeftPointer(context: CanvasRenderingContext2D): void {
    context.moveTo(0.9, 0.1);
    context.lineTo(0.9, 0.9);
    context.lineTo(0.1, 0.5);
    context.fill();
  }

  static #drawLower(context: CanvasRenderingContext2D): void {
    context.rect(0, 0.5, 1, 0.5);
    context.fill();
  }

  static #drawRight(context: CanvasRenderingContext2D): void {
    context.rect(0.5, 0, 0.5, 1);
    context.fill();
  }

  static #drawPointer(context: CanvasRenderingContext2D): void {
    context.moveTo(0.1, 0.1);
    context.lineTo(0.1, 0.9);
    context.lineTo(0.9, 0.5);
    context.fill();
  }

  static #drawRectangleLine(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.15;
    context.rect(0.25, 0.25, 0.5, 0.5);
    context.stroke();
  }

  static #drawRectangle(context: CanvasRenderingContext2D): void {
    context.rect(0.25, 0.25, 0.5, 0.5);
    context.fill();
  }

  static #drawRightArrow(context: CanvasRenderingContext2D): void {
    context.moveTo(0.1, 0.7);
    context.lineTo(0.55, 0.7);
    context.lineTo(0.55, 0.95);
    context.lineTo(0.95, 0.5);
    context.lineTo(0.55, 0.05);
    context.lineTo(0.55, 0.3);
    context.lineTo(0.1, 0.3);
    context.lineTo(0.1, 0.7);
    context.fill();
  }

  static #drawRightPointer(context: CanvasRenderingContext2D): void {
    context.moveTo(0.1, 0.1);
    context.lineTo(0.1, 0.9);
    context.lineTo(0.9, 0.5);
    context.fill();
  }

  static #drawSlash(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.25;
    context.moveTo(1, 0);
    context.lineTo(0, 1);
    context.stroke();
  }

  static #drawStripe(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.25;
    context.moveTo(0.5, 0);
    context.lineTo(0.5, 1);
    context.stroke();
  }

  static #drawTriangleLine(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.15;
    context.moveTo(0.2, 0.8);
    context.lineTo(0.5, 0.2);
    context.lineTo(0.8, 0.8);
    context.closePath();
    context.stroke();
  }

  static #drawTriangle(context: CanvasRenderingContext2D): void {
    context.moveTo(0.2, 0.8);
    context.lineTo(0.5, 0.2);
    context.lineTo(0.8, 0.8);
    context.fill();
  }

  static #drawTriangleTurned(context: CanvasRenderingContext2D): void {
    context.moveTo(0.2, 0.2);
    context.lineTo(0.5, 0.8);
    context.lineTo(0.8, 0.2);
    context.fill();
  }

  static #drawTurnedT(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.2;
    context.moveTo(0.1, 0.8);
    context.lineTo(0.9, 0.8);
    context.moveTo(0.5, 0.2);
    context.lineTo(0.5, 0.8);
    context.stroke();
  }

  static #drawUpper(context: CanvasRenderingContext2D): void {
    context.rect(0, 0, 1, 0.5);
    context.fill();
  }

  static #drawUpperBowl(context: CanvasRenderingContext2D): void {
    context.moveTo(0.05, 0.5);
    context.lineTo(0.95, 0.5);
    // context.arc_negative(0.5,0.5,0.45,0,Math.PI);
    context.arc(0.5, 0.5, 0.45, 0, -Math.PI);
    context.fill();
  }

  static #drawX(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.25;
    context.moveTo(1, 0);
    context.lineTo(0, 1);
    context.moveTo(0, 0);
    context.lineTo(1, 1);
    context.stroke();
  }

  static #drawHexagon(context: CanvasRenderingContext2D): void {
    context.moveTo(0.8, 0.5);
    context.lineTo(0.65, 0.24);
    context.lineTo(0.35, 0.24);
    context.lineTo(0.2, 0.5);
    context.lineTo(0.35, 0.76);
    context.lineTo(0.65, 0.76);
    context.fill();
  }

  static #drawShell(context: CanvasRenderingContext2D): void {
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

  static #drawShellModern(context: CanvasRenderingContext2D): void {
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
}
