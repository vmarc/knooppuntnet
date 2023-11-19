import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { ViewChild } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteGapBuilder } from './monitor-route-gap-builder';

@Component({
  selector: 'kpn-monitor-route-gap-canvas',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <canvas #gapCanvas [height]="height" [width]="width"></canvas> `,
  styles: `
    canvas {
      display: block;
    }
  `,
  standalone: true,
})
export class MonitorRouteGapCanvasComponent implements AfterViewInit {
  @Input({ required: true }) description: string;
  @Input({ required: true }) osmSegmentCount: number;
  @Input({ required: true }) height: number;
  @ViewChild('gapCanvas') canvas!: ElementRef<HTMLCanvasElement>;

  width = 40;

  ngAfterViewInit(): void {
    new MonitorRouteGapBuilder(this.canvas, this.description, this.osmSegmentCount).draw();
  }
}
