import {Component, Input} from "@angular/core";

@Component({
  selector: 'kpn-link-subset-orphan-routes',
  template: `
    <a routerLink="{{'/analysis/orphan-routes/' + country + '/' + networkType}}">Orphan routes</a>
  `
})
export class LinkSubsetOrphanRoutesComponent {
  @Input() country: string;
  @Input() networkType: string;
}
