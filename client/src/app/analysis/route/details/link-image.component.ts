import { ChangeDetectionStrategy } from '@angular/core';
import { Renderer2 } from '@angular/core';
import { AfterViewChecked } from '@angular/core';
import { ElementRef } from '@angular/core';
import { ViewChild } from '@angular/core';
import { Component, Input } from '@angular/core';

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
})
export class LinkImageComponent implements AfterViewChecked {
  @Input() linkName: string;
  @ViewChild('image', { static: false }) imageRef: ElementRef;
  @ViewChild('imageWrapper', { static: false }) divRef: ElementRef;

  constructor(private renderer: Renderer2) {}

  ngAfterViewChecked(): void {
    if (this.imageRef && this.divRef) {
      const height =
        this.divRef.nativeElement.parentElement.parentElement.offsetHeight;
      this.renderer.setStyle(
        this.imageRef.nativeElement,
        'height',
        `${height}px`
      );
    }
  }
}
