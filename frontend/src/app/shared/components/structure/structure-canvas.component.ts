import { effect } from '@angular/core';
import { input } from '@angular/core';
import { viewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { ElementRef } from '@angular/core';
import { Component } from '@angular/core';
import { MemberType } from '@api/common/data/member-type';
import { Link } from '@api/common/route/link';
import { DrawStructure } from '@app/shared/components/structure/draw-structure';

@Component({
  selector: 'ui-structure-canvas',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '<canvas #gapCanvas  [height]="height()" width="40"></canvas>',
  styles: `
    canvas {
      display: block;
    }
  `,
})
export class StructureCanvasComponent {
  readonly segmentIds = input.required<number[]>();
  readonly memberType = input.required<MemberType>();
  readonly link = input.required<Link>();
  readonly height = input.required<number>();
  private readonly canvas = viewChild<ElementRef<HTMLCanvasElement>>('gapCanvas');

  constructor() {
    effect(() => {
      const memberType = this.memberType();
      const link = this.link();
      const canvas = this.canvas()?.nativeElement;
      const height = this.height();
      if (memberType && canvas && height > 0) {
        setTimeout(
          () => new DrawStructure(canvas, height, this.segmentIds(), memberType, link).draw(),
          0
        );
      }
    });
  }
}
