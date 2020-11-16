import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {List} from 'immutable';
import {Ref} from '../../../kpn/api/common/common/ref';

@Component({
  selector: 'kpn-network-fact-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span *ngIf="routes.size == 1" i18n="@@network-facts.route">
      Route:
    </span>
    <span *ngIf="routes.size > 1" i18n="@@network-facts.routes">
      Routes:
    </span>
    <div class="kpn-comma-list">
      <kpn-link-route
        *ngFor="let route of routes"
        [routeId]="route.id"
        [title]="route.name">
      </kpn-link-route>
    </div>
  `
})
export class NetworkFactRoutesComponent {
  @Input() routes: List<Ref>;
}
