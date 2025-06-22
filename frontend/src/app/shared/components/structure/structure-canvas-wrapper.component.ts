import { viewChild } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { signal } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MemberType } from '@api/common/data/member-type';
import { Link } from '@api/common/route/link';
import { fromEvent } from 'rxjs';
import { Subscription } from 'rxjs';
import { StructureCanvasComponent } from './structure-canvas.component';

@Component({
  selector: 'ui-structure-canvas-wrapper',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div #canvasWrapper class="canvasWrapper">
      @if (height() > 0) {
        <ui-structure-canvas
          [height]="height()"
          [segmentIds]="segmentIds()"
          [memberType]="memberType()"
          [link]="link()"
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
  imports: [StructureCanvasComponent],
})
export class StructureCanvasWrapperComponent implements AfterViewInit, OnInit, OnDestroy {
  readonly segmentIds = input<number[]>([]);
  readonly memberType = input.required<MemberType>();
  readonly link = input.required<Link>();

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
