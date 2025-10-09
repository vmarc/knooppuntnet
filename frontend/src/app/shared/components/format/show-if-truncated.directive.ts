import { inject } from '@angular/core';
import { HostListener } from '@angular/core';
import { ElementRef } from '@angular/core';
import { Directive } from '@angular/core';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';

@Directive({
  // eslint-disable-next-line @angular-eslint/directive-selector
  selector: '[nz-tooltip][showIfTruncated]',
})
export class ShowIfTruncatedDirective {
  private readonly tooltip = inject(NzTooltipDirective);
  private readonly elementRef = inject(ElementRef<HTMLElement>);

  @HostListener('mouseenter', ['$event'])
  setTooltipState(): void {
    const element = this.elementRef.nativeElement;
    this.tooltip.directiveContent = element.textContent;
    this.tooltip.visible = element.clientWidth < element.scrollWidth;
  }
}
