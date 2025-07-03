import { viewChild } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { signal } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteGapCanvasComponent } from './route-gap-canvas.component';
import { fromEvent } from 'rxjs';
import { Subscription } from 'rxjs';

@Component({
  selector: 'ui-route-gap',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div #canvasWrapper class="canvasWrapper">
      @if (height() > 0) {
        <ui-route-gap-canvas
          [height]="height()"
          [description]="description()"
          [osmSegmentCount]="osmSegmentCount()"
        />
      }
    </div>
  `,
  styles: `
    .canvasWrapper {
      display: flex;
      justify-content: center;
    }
  `,
  imports: [RouteGapCanvasComponent],
})
export class RouteGapComponent implements AfterViewInit, OnInit, OnDestroy {
  readonly description = input.required<string>();
  readonly osmSegmentCount = input.required<number>();

  private readonly canvasWrapper = viewChild<ElementRef<HTMLDivElement>>('canvasWrapper');

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
    const gapComponent = this.canvasWrapper().nativeElement.parentElement;
    const td = gapComponent.parentElement;
    const tr = td.parentElement;
    return tr.offsetHeight - 1;
  }
}
