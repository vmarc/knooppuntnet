import { Input } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { OnInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { ViewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SymbolBuilder } from './symbol-builder';
import { SymbolDescription } from './symbol-description';
import { SymbolHikerComponent } from './symbol-hiker.component';
import { SymbolParser } from './symbol-parser';
import { SymbolWheelComponent } from './symbol-wheel.component';

@Component({
  selector: 'kpn-symbol',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [style]="box" class="box">
      <canvas #symbolCanvas [width]="width" [height]="height"></canvas>
      @if (isForegroundHiker()) {
        <kpn-symbol-hiker [width]="width" [height]="height" [color]="foregroundColor()" />
      }
      @if (isForeground2Hiker()) {
        <kpn-symbol-hiker [width]="width" [height]="height" [color]="foreground2Color()" />
      }
      @if (isForegroundWheel()) {
        <kpn-symbol-wheel [width]="width" [height]="height" [color]="foregroundColor()" />
      }
      @if (isForeground2Wheel()) {
        <kpn-symbol-wheel [width]="width" [height]="height" [color]="foreground2Color()" />
      }
    </div>
  `,
  styles: `
    .box {
      position: relative;
    }

    canvas {
      position: absolute;
      border: 1px solid lightgray;
      letter-spacing: 0;
    }

    kpn-symbol-hiker {
      position: absolute;
    }

    kpn-symbol-wheel {
      position: absolute;
    }
  `,
  standalone: true,
  imports: [SymbolHikerComponent, SymbolWheelComponent],
})
export class SymbolComponent implements OnInit, AfterViewInit {
  @Input({ required: true }) description: string;
  @Input({ required: false }) width = 50;
  @Input({ required: false }) height = 50;
  @Input({ required: false }) grid = false;
  @ViewChild('symbolCanvas') canvas!: ElementRef<HTMLCanvasElement>;

  box = '';
  symbolDescription: SymbolDescription;

  ngOnInit(): void {
    this.box = `width: ${this.width}px; height: ${this.height}px;`;
    this.symbolDescription = new SymbolParser().parse(this.description);
  }

  ngAfterViewInit(): void {
    const sb = new SymbolBuilder(this.canvas);
    if (this.grid) {
      sb.drawGrid();
    }
    sb.draw(this.symbolDescription);
  }

  isForegroundHiker(): boolean {
    return this.symbolDescription.foreground?.shape === 'hiker';
  }

  isForegroundWheel(): boolean {
    return this.symbolDescription.foreground?.shape === 'wheel';
  }

  foregroundColor(): string {
    return this.symbolDescription.foreground?.color ?? 'black';
  }

  isForeground2Hiker(): boolean {
    return this.symbolDescription.foreground2?.shape === 'hiker';
  }

  isForeground2Wheel(): boolean {
    return this.symbolDescription.foreground2?.shape === 'wheel';
  }

  foreground2Color(): string {
    return this.symbolDescription.foreground2?.color ?? 'black';
  }
}
