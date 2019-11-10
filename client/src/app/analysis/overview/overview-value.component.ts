import {Component, Input} from "@angular/core";
import {Subset} from "../../kpn/api/custom/subset";
import {Stat} from "./stat";

@Component({
  selector: "kpn-overview-value",
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
export class OverviewValueComponent {

  @Input() stat: Stat;
  @Input() subset: Subset;

  hasLink() {
    return this.stat.configuration.linkFunction !== null;
  }

  value() {
    return this.stat.value(this.subset);
  }

  link() {
    return "/analysis/" + this.stat.configuration.linkFunction(this.stat.configuration.fact, this.subset);
  }

}
