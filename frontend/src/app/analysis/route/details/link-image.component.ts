import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Renderer2 } from '@angular/core';
import { AfterViewChecked } from '@angular/core';
import { ElementRef } from '@angular/core';
import { ViewChild } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Subscription } from 'rxjs';
import { fromEvent } from 'rxjs';

@Component({
  selector: 'kpn-link-image',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div style="height: 100%;" #imageWrapper>
      <img
        style="width: 40px;  display:block;"
        #image
        [src]="'/assets/images/links/' + linkName + '.png'"
        [alt]="linkName"
      />
    </div>
  `,
  standalone: true,
})
export class LinkImageComponent implements OnInit, OnDestroy, AfterViewChecked {
  @Input() linkName: string;
  @ViewChild('image', { static: false }) imageRef: ElementRef;
  @ViewChild('imageWrapper', { static: false }) divRef: ElementRef;

  private readonly renderer = inject(Renderer2);

  private resizeSubscription$: Subscription;

  ngOnInit(): void {
    this.resizeSubscription$ = fromEvent(window, 'resize').subscribe(() => {
      this.adjustImageHeight();
    });
  }

  ngOnDestroy(): void {
    this.resizeSubscription$.unsubscribe();
  }

  ngAfterViewChecked(): void {
    this.adjustImageHeight();
  }

  private adjustImageHeight(): void {
    if (this.imageRef && this.divRef) {
      const height = this.divRef.nativeElement.parentElement.parentElement.offsetHeight - 1;
      this.renderer.setStyle(this.imageRef.nativeElement, 'height', `${height}px`);
    }
  }
}
