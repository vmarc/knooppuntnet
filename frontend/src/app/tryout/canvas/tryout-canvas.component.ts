import { effect } from '@angular/core';
import { input } from '@angular/core';
import { viewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { ElementRef } from '@angular/core';
import { Component } from '@angular/core';
import { Link } from '@api/common/route/link';
import { TryoutLinkBuilder } from './tryout-link-builder';

@Component({
  selector: 'kpn-tryout-canvas',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '<canvas #gapCanvas  [height]="height()" width="40"></canvas>',
  styles: `
    canvas {
      display: block;
    }
  `,
})
export class TryoutCanvasComponent {
  link = input.required<Link>();
  height = input.required<number>();
  private readonly canvas = viewChild<ElementRef<HTMLCanvasElement>>('gapCanvas');

  constructor() {
    effect(() => {
      const link = this.link();
      const canvas = this.canvas()?.nativeElement;
      const height = this.height();
      if (link && canvas && height > 0) {
        setTimeout(() => new TryoutLinkBuilder(canvas, height, link).draw(), 0);
      } else {
        console.log(`height=${height}`);
      }
    });
  }
}
