import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'kpn-no-route-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title i18n="@@no-route-dialog.title">No route</div>
      <div mat-dialog-content i18n="@@no-route-dialog.message">
        You clicked the button to zoom to fit the complete route on the map, but
        there is no start- and endnode for the route defined yet. Please plan a
        route first.
      </div>
    </kpn-dialog>
  `,
})
export class NoRouteDialogComponent {}
