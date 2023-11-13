import { NgSwitchDefault } from '@angular/common';
import { NgSwitchCase } from '@angular/common';
import { NgSwitch } from '@angular/common';
import { NgFor } from '@angular/common';
import { Input } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-monitor-route-form-errors',
  template: `
    <p *ngFor="let error of errors">
      <ng-container [ngSwitch]="error">
        <span
          *ngSwitchCase="'no-relation-id'"
          i18n="@@monitor.route.save-dialog.no-relation-id"
        >
          Note: we cannot yet perform an analysis. The reference information is
          still incomplete. The relation id has not been specified.
        </span>
        <span
          *ngSwitchCase="'osm-relation-not-found'"
          i18n="@@monitor.route.save-dialog.osm-relation-not-found"
        >
          Note: we cannot yet perform an analysis. The reference information is
          still incomplete. No route with given relation id was found at given
          reference date.
        </span>
        <span *ngSwitchDefault>
          {{ error }}
        </span>
      </ng-container>
    </p>
  `,
  standalone: true,
  imports: [NgFor, NgSwitch, NgSwitchCase, NgSwitchDefault],
})
export class MonitorRouteFormErrorsComponent {
  @Input({ required: true }) errors: string[];
}
