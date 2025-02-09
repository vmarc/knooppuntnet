import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { MAP_SERVICE_TOKEN } from '@app/ol/services/openlayers-map-service';
import { State } from '@app/state/state';
import { PlannerMapService } from '@app/planner/pages/planner/planner-map.service';
import { PlannerSidebarComponent } from '@app/planner/pages/planner/sidebar/planner-sidebar.component';

@Component({
  selector: 'kpn-planner',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button mat-icon-button routerLink="/">
      <mat-icon svgIcon="back" />
    </button>
    <kpn-planner-sidebar />
  `,
  providers: [
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: PlannerMapService,
    },
  ],
  imports: [MatIcon, MatIconButton, RouterLink, PlannerSidebarComponent],
})
export class PlannerComponent implements OnInit {
  private readonly state = inject(State);

  ngOnInit(): void {
    this.state.map.updateSubject('plan');
  }
}
