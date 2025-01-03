import { viewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { ElementRef } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-tryout-canvas',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '<canvas #gapCanvas [height]="40" width="40"></canvas>',
  styles: `
    canvas {
      display: block;
    }
  `,
})
export class TryoutCanvasComponent {
  // description = input.required<string>();
  // osmSegmentCount = input.required<number>();

  // private _height: number;
  // get height(): number {
  //   return this._height;
  // }
  //
  // @Input({ required: true }) set height(value: number) {
  //   this._height = value;
  //   setTimeout(() => this.draw(), 0);
  // }

  private readonly canvas = viewChild<ElementRef<HTMLCanvasElement>>('gapCanvas');

  private draw(): void {
    // new MonitorRouteGapBuilder(this.canvas(), this.description(), this.osmSegmentCount()).draw();
  }
}
