import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {Reference} from "../../../kpn/api/common/common/reference";
import {List} from "immutable";

@Component({
  selector: "kpn-route-network-references",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="references.isEmpty()" i18n="@@route.no-network-references">None</div>
    <div *ngFor="let reference of references" class="kpn-line">
      <kpn-network-type-icon [networkType]="reference.networkType"></kpn-network-type-icon>
      <a [routerLink]="'/analysis/network/' + reference.id">{{reference.name}}</a>
    </div>
  `
})
export class RouteNetworkReferencesComponent {
  @Input() references: List<Reference>;
}
