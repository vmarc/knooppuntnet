export class SymbolGrid {
  static draw(context: CanvasRenderingContext2D): void {
    context.lineWidth = 0.005;
    context.strokeStyle = 'lightgray';
    for (let x = 0; x < 1; x += 0.1) {
      context.beginPath();
      context.moveTo(x, 0);
      context.lineTo(x, 1);
      context.stroke();
      context.closePath();
    }
    for (let y = 0; y < 1; y += 0.1) {
      context.beginPath();
      context.moveTo(0, y);
      context.lineTo(1, y);
      context.stroke();
      context.closePath();
    }
  }
}
