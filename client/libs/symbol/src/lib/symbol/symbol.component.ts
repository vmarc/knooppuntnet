import { Input } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { ViewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SymbolBuilder } from './symbol-builder';

@Component({
  selector: 'kpn-symbol',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <canvas #symbolCanvas [width]="width" [height]="height"></canvas>
  `,
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
  @Input({ required: true }) description: string;
  @Input({ required: false }) width = 50;
  @Input({ required: false }) height = 50;
  @Input({ required: false }) grid = false;
  @ViewChild('symbolCanvas') canvas!: ElementRef<HTMLCanvasElement>;

  ngAfterViewInit(): void {
    const sb = new SymbolBuilder(this.canvas);
    if (this.grid) {
      sb.drawGrid();
    }
    sb.draw(this.description);
  }
}
