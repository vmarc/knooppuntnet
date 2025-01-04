import { viewChild } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { signal } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LinkInfo } from '@api/common/route/link-info';
import { fromEvent } from 'rxjs';
import { Subscription } from 'rxjs';
import { TryoutCanvasComponent } from './tryout-canvas.component';

@Component({
  selector: 'kpn-tryout-wrapper',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div #canvasWrapper class="canvasWrapper">
      @if (height() > 0) {
        <kpn-tryout-canvas [height]="height()" [linkInfo]="linkInfo()" />
      }
    </div>
  `,
  styles: `
    .canvasWrapper {
      display: flex;
      justify-content: center;
    }
  `,
  imports: [TryoutCanvasComponent],
})
export class TryoutWrapperComponent implements AfterViewInit, OnInit, OnDestroy {
  linkInfo = input.required<LinkInfo>();

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
