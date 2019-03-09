import {Component, Input} from '@angular/core';
import {Stat} from "./stat";
import {Subset} from "../../../kpn/shared/subset";

@Component({
  selector: 'kpn-overview-table-cell',
  template: `
    <a *ngIf="hasLink()" [routerLink]="link()">{{value()}}</a>
    <span *ngIf="!hasLink()">{{value()}}</span>
  `,
  styles: [`
    :host {
      display: contents;
    }
  `]
})
export class OverviewTableCellComponent {

  @Input() stat: Stat;
  @Input() subset: Subset;

  hasLink() {
    return this.stat.configuration.linkFunction !== null;
  }

  value() {
    return this.stat.value(this.subset);
  }

  link() {
    return "../" + this.stat.configuration.linkFunction(this.stat.configuration.id, this.subset);
  }

}
