import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit, Component, ElementRef, Input } from '@angular/core';
import { Subset } from '@api/custom/subset';
import { StatisticConfiguration } from './statistic-configuration';

@Component({
  selector: 'kpn-statistic-configuration',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <ng-content></ng-content> `,
})
export class StatisticConfigurationComponent implements AfterViewInit {
  @Input() id: string;
  @Input() fact: string;
  @Input() name: string;
  @Input() markdownEnabled = false;
  @Input() linkFunction: (id: string, subset: Subset) => string | null = null;

  comment: string;

  constructor(private element: ElementRef) {}

  ngAfterViewInit(): void {
    this.comment = this.element.nativeElement.textContent;
  }

  toStatistic() {
    return new StatisticConfiguration(
      this.id,
      this.fact,
      this.name,
      this.markdownEnabled,
      this.comment,
      this.linkFunction
    );
  }
}
