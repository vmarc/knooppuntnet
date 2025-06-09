import { viewChild } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { OnInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { SymbolBuilder } from './internal/symbol-builder';
import { SymbolDescription } from './internal/symbol-description';
import { SymbolHikerComponent } from './internal/symbol-hiker.component';
import { SymbolParser } from './internal/symbol-parser';
import { SymbolWheelComponent } from './internal/symbol-wheel.component';

@Component({
  selector: 'ui-symbol',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [style]="box" class="box">
      <canvas #symbolCanvas [width]="width()" [height]="height()"></canvas>
      @if (isForegroundHiker()) {
        <ui-symbol-hiker [width]="width()" [height]="height()" [color]="foregroundColor()" />
      }
      @if (isForeground2Hiker()) {
        <ui-symbol-hiker [width]="width()" [height]="height()" [color]="foreground2Color()" />
      }
      @if (isForegroundWheel()) {
        <ui-symbol-wheel [width]="width()" [height]="height()" [color]="foregroundColor()" />
      }
      @if (isForeground2Wheel()) {
        <ui-symbol-wheel [width]="width()" [height]="height()" [color]="foreground2Color()" />
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
  imports: [SymbolHikerComponent, SymbolWheelComponent],
})
export class SymbolComponent implements OnInit, AfterViewInit {
  readonly description = input.required<string>();
  readonly width = input(50);
  readonly height = input(50);
  readonly grid = input(false);
  private readonly canvas = viewChild<ElementRef<HTMLCanvasElement>>('symbolCanvas');

  box = '';
  symbolDescription: SymbolDescription;

  ngOnInit(): void {
    this.box = `width: ${this.width()}px; height: ${this.height()}px;`;
    this.symbolDescription = new SymbolParser().parse(this.description());
  }

  ngAfterViewInit(): void {
    const sb = new SymbolBuilder(this.canvas().nativeElement, this.width(), this.height());
    if (this.grid()) {
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
