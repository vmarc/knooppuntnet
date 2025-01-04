import { effect } from '@angular/core';
import { input } from '@angular/core';
import { viewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { ElementRef } from '@angular/core';
import { Component } from '@angular/core';
import { LinkInfo } from '@api/common/route/link-info';
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
  linkInfo = input.required<LinkInfo>();
  height = input.required<number>();
  private readonly canvas = viewChild<ElementRef<HTMLCanvasElement>>('gapCanvas');

  constructor() {
    effect(() => {
      const linkInfo = this.linkInfo();
      const canvas = this.canvas()?.nativeElement;
      const height = this.height();
      if (linkInfo && canvas && height > 0) {
        console.log(`link=${linkInfo.name}, height=${height}`);
        setTimeout(() => new TryoutLinkBuilder(canvas, height, linkInfo.link).draw(), 0);
      } else {
        console.log(`height=${height}`);
      }
    });
  }
}
