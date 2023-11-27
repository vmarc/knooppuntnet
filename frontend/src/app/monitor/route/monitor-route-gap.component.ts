import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { signal } from '@angular/core';
import { Input } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { ViewChild } from '@angular/core';
import { Component } from '@angular/core';
import { fromEvent } from 'rxjs';
import { Subscription } from 'rxjs';
import { MonitorRouteGapCanvasComponent } from './monitor-route-gap-canvas.component';

@Component({
  selector: 'kpn-monitor-route-gap',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div #canvasWrapper class="canvasWrapper">
      @if (height() > 0) {
        <kpn-monitor-route-gap-canvas
          [height]="height()"
          [description]="description"
          [osmSegmentCount]="osmSegmentCount"
        />
      }
    </div>
  `,
  styles: `
    .canvasWrapper {
      display: flex;
      justify-content: center
    }
  `,
  standalone: true,
  imports: [MonitorRouteGapCanvasComponent],
})
export class MonitorRouteGapComponent implements AfterViewInit, OnInit, OnDestroy {
  @Input({ required: true }) description: string;
  @Input({ required: true }) osmSegmentCount: number;
  @ViewChild('canvasWrapper') canvasWrapper!: ElementRef<HTMLDivElement>;

  height = signal(0);

  private resizeSubscription$: Subscription;

  ngAfterViewInit(): void {
    setTimeout(() => this.height.set(this.rowHeight()), 0);
  }

  ngOnInit(): void {
    this.resizeSubscription$ = fromEvent(window, 'resize').subscribe(() => {
      this.height.set(this.rowHeight());
    });
  }

  ngOnDestroy(): void {
    this.resizeSubscription$.unsubscribe();
  }

  private rowHeight(): number {
    const gapComponent = this.canvasWrapper.nativeElement.parentElement;
    const td = gapComponent.parentElement;
    const tr = td.parentElement;
    return tr.offsetHeight - 1;
  }
}
