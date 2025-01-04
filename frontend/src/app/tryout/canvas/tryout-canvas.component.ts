import { Input } from '@angular/core';
import { AfterViewInit } from '@angular/core';
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
  template: '<canvas #gapCanvas  [height]="height" width="40"></canvas>',
  styles: `
    canvas {
      display: block;
    }
  `,
})
export class TryoutCanvasComponent implements AfterViewInit {
  linkInfo = input.required<LinkInfo>();

  private _height: number;
  get height(): number {
    return this._height;
  }

  @Input({ required: true }) set height(value: number) {
    this._height = value;
    setTimeout(() => this.draw(), 0);
  }

  private readonly canvas = viewChild<ElementRef<HTMLCanvasElement>>('gapCanvas');

  ngAfterViewInit(): void {
    setTimeout(() => this.draw(), 0);
  }

  draw(): void {
    new TryoutLinkBuilder(this.canvas(), this.linkInfo()).draw();
  }
}
