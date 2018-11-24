import {Component, Input} from "@angular/core";
import {Reference} from "../../../kpn/shared/common/reference";
import {List} from "immutable";

@Component({
  selector: 'node-routes',
  template: `
    <p *ngIf="routes.isEmpty()">None</p> <!-- Geen -->
    <p *ngFor="let route of routes">
      <icon-route-link [route]="route"></icon-route-link>
    </p>
  `
})
export class NodeRoutesComponent {
  @Input() routes: List<Reference> = List();
}
