import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { signal } from '@angular/core';
import { Input } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { ViewChild } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteGapCanvasComponent } from './monitor-route-gap-canvas.component';

@Component({
  selector: 'kpn-monitor-route-gap',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div #canvasWrapper class="canvasWrapper">
      <kpn-monitor-route-gap-canvas
        *ngIf="height() > 0"
        [height]="height()"
        [description]="description"
        [osmSegmentCount]="osmSegmentCount"
      />
    </div>
  `,
  styles: `
    .canvasWrapper {
      display: flex;
      justify-content: center
    }
  `,
  standalone: true,
  imports: [NgIf, MonitorRouteGapCanvasComponent],
})
export class MonitorRouteGapComponent implements AfterViewInit {
  @Input({ required: true }) description: string;
  @Input({ required: true }) osmSegmentCount: number;
  @ViewChild('canvasWrapper') canvasWrapper!: ElementRef<HTMLDivElement>;

  height = signal(0);

  ngAfterViewInit(): void {
    const gapComponent = this.canvasWrapper.nativeElement.parentElement;
    const td = gapComponent.parentElement;
    const tr = td.parentElement;
    const rowHeight = tr.offsetHeight;
    setTimeout(() => this.height.set(rowHeight), 0);
  }
}
