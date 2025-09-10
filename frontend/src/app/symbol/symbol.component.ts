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
      <canvas #symbolCanvas [width]="width()" [height]="height()" class="box-item"></canvas>
      @if (isForegroundHiker()) {
        <ui-symbol-hiker
          [width]="width()"
          [height]="height()"
          [color]="foregroundColor()"
          class="box-item"
        />
      }
      @if (isForeground2Hiker()) {
        <ui-symbol-hiker
          [width]="width()"
          [height]="height()"
          [color]="foreground2Color()"
          class="box-item"
        />
      }
      @if (isForegroundWheel()) {
        <ui-symbol-wheel
          [width]="width()"
          [height]="height()"
          [color]="foregroundColor()"
          class="box-item"
        />
      }
      @if (isForeground2Wheel()) {
        <ui-symbol-wheel
          [width]="width()"
          [height]="height()"
          [color]="foreground2Color()"
          class="box-item"
        />
      }
    </div>
  `,
  styles: `
    .box {
      display: inline-grid;
      grid-template-columns: 25px;
      grid-template-rows: 25px;
    }

    .box-item {
      grid-column-start: 1;
      grid-column-end: 1;
      grid-row-start: 1;
      grid-row-end: 1;
    }

    canvas {
      border: 1px solid lightgray;
      letter-spacing: 0;
    }
  `,
  imports: [SymbolHikerComponent, SymbolWheelComponent],
})
export class SymbolComponent implements OnInit, AfterViewInit {
  readonly description = input.required<string>();
  readonly width = input(50);
  readonly height = input(50);
  readonly grid = input(false);
  private readonly canvas = viewChild<ElementRef>('symbolCanvas');

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
