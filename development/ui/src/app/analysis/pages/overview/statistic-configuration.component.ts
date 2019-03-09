import {AfterViewInit, Component, ElementRef, Input} from '@angular/core';
import {StatisticConfiguration} from "./statistic-configuration";
import {Subset} from "../../../kpn/shared/subset";

@Component({
  selector: 'kpn-statistic-configuration',
  template: `
    <ng-content></ng-content>
  `
})
export class StatisticConfigurationComponent implements AfterViewInit {

  @Input() id: string;
  @Input() name: string;
  @Input() markdownEnabled: boolean = false;
  @Input() linkFunction: (id: string, subset: Subset) => string | null = null;

  comment: string;

  constructor(private element: ElementRef) {
  }

  ngAfterViewInit(): void {
    this.comment = this.element.nativeElement.textContent;
  }

  toStatistic() {
    return new StatisticConfiguration(this.id, this.name, this.markdownEnabled, this.comment, this.linkFunction);
  }

}
