import { Input } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { ViewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SymbolGrid } from './symbol-grid';
import { SymbolShape } from './symbol-shape';

@Component({
  selector: 'kpn-symbol',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <canvas #symbolCanvas width="100" height="100"></canvas> `,
  styles: [
    `
      canvas {
        border: 1px solid lightgray;
      }
    `,
  ],
  standalone: true,
})
export class SymbolComponent implements AfterViewInit {
  @Input({ required: true }) shape: string;
  @ViewChild('symbolCanvas') canvas!: ElementRef<HTMLCanvasElement>;

  ngAfterViewInit(): void {
    const context = this.canvas.nativeElement.getContext('2d');
    context.scale(
      this.canvas.nativeElement.width,
      this.canvas.nativeElement.height
    );

    SymbolGrid.draw(context);

    context.beginPath();
    context.strokeStyle = 'grey';
    context.fillStyle = 'grey';
    SymbolShape.draw(context, this.shape);
    context.closePath();
  }
}
