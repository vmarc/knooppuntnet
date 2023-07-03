import { ElementRef } from '@angular/core';
import { SymbolDescription } from './symbol-description';
import { SymbolImage } from './symbol-image';
import { SymbolParser } from './symbol-parser';
import { SymbolShape } from './symbol-shape';

export class SymbolBuilder {
  private context: CanvasRenderingContext2D;

  constructor(canvas: ElementRef<HTMLCanvasElement>) {
    this.context = canvas.nativeElement.getContext('2d');
    this.context.scale(canvas.nativeElement.width, canvas.nativeElement.height);
  }

  drawGrid(): void {
    this.context.lineWidth = 0.005;
    this.context.strokeStyle = 'lightgray';
    for (let x = 0; x < 1; x += 0.1) {
      this.context.beginPath();
      this.context.moveTo(x, 0);
      this.context.lineTo(x, 1);
      this.context.stroke();
      this.context.closePath();
    }
    for (let y = 0; y < 1; y += 0.1) {
      this.context.beginPath();
      this.context.moveTo(0, y);
      this.context.lineTo(1, y);
      this.context.stroke();
      this.context.closePath();
    }
  }

  draw(descriptionString: string): void {
    const description = new SymbolParser().parse(descriptionString);
    this.drawBackground(description);
    this.drawImage(description.foreground);
    this.drawImage(description.foreground2);
    this.drawText(description);
  }

  private drawBackground(description: SymbolDescription): void {
    if (description.background) {
      this.context.beginPath();
      let backgroundColor = 'white';
      if (description.background.color) {
        backgroundColor = description.background.color;
      }
      this.context.strokeStyle = backgroundColor;
      this.context.fillStyle = backgroundColor;
      if (description.background.shape) {
        SymbolShape.drawBackground(this.context, description.background.shape);
      } else {
        this.context.rect(0, 0, 1, 1);
        this.context.fill();
      }
      this.context.closePath();
    }
  }

  private drawImage(image: SymbolImage): void {
    if (image) {
      this.context.beginPath();
      let foregroundColor = 'white';
      if (image.color) {
        foregroundColor = image.color;
      }
      this.context.strokeStyle = foregroundColor;
      this.context.fillStyle = foregroundColor;
      if (image.shape) {
        SymbolShape.drawForeground(this.context, image.shape);
      } else {
        this.context.rect(0, 0, 1, 1);
        this.context.fill();
      }
      this.context.closePath();
    }
  }

  private drawText(description: SymbolDescription): void {
    if (description.text) {
      this.context.beginPath();
      let textColor = 'black';
      if (description.textcolor) {
        textColor = description.textcolor;
      }
      this.context.textAlign = 'center';
      this.context.textBaseline = 'middle';
      this.context.fillStyle = textColor;
      this.context.font = 'normal 700 0.4px Arial';
      this.context.fillText(description.text, 0.5, 0.53);
      this.context.closePath();
    }
  }
}
