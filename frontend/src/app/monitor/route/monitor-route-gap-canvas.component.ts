import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { ElementRef } from '@angular/core';
import { ViewChild } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteGapBuilder } from './monitor-route-gap-builder';

@Component({
  selector: 'kpn-monitor-route-gap-canvas',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <canvas #gapCanvas [height]="height" width="40"></canvas> `,
  styles: `
    canvas {
      display: block;
    }
  `,
  standalone: true,
})
export class MonitorRouteGapCanvasComponent {
  @Input({ required: true }) description: string;
  @Input({ required: true }) osmSegmentCount: number;

  private _height: number;
  get height(): number {
    return this._height;
  }

  @Input({ required: true }) set height(value: number) {
    this._height = value;
    setTimeout(() => this.draw(), 0);
  }

  @ViewChild('gapCanvas') canvas!: ElementRef<HTMLCanvasElement>;

  private draw(): void {
    new MonitorRouteGapBuilder(
      this.canvas,
      this.description,
      this.osmSegmentCount
    ).draw();
  }
}
