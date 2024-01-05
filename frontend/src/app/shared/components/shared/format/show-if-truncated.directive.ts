import { inject } from '@angular/core';
import { HostListener } from '@angular/core';
import { ElementRef } from '@angular/core';
import { Directive } from '@angular/core';
import { MatTooltip } from '@angular/material/tooltip';

@Directive({
  selector: '[matTooltip][showIfTruncated]',
  standalone: true,
})
export class ShowIfTruncatedDirective {
  private readonly matTooltip = inject(MatTooltip);
  private readonly elementRef = inject(ElementRef<HTMLElement>);

  @HostListener('mouseenter', ['$event'])
  setTooltipState(): void {
    const element = this.elementRef.nativeElement;
    this.matTooltip.message = element.textContent;
    this.matTooltip.disabled = element.scrollWidth <= element.clientWidth;
  }
}
