import { HostListener } from '@angular/core';
import { ElementRef } from '@angular/core';
import { Directive } from '@angular/core';
import { MatTooltip } from '@angular/material/tooltip';

@Directive({
  selector: '[matTooltip][showIfTruncated]',
  standalone: true,
})
export class ShowIfTruncatedDirective {
  constructor(
    private matTooltip: MatTooltip,
    private elementRef: ElementRef<HTMLElement>
  ) {}

  @HostListener('mouseenter', ['$event'])
  setTooltipState(): void {
    const element = this.elementRef.nativeElement;
    this.matTooltip.message = element.textContent;
    this.matTooltip.disabled = element.scrollWidth <= element.clientWidth;
  }
}
