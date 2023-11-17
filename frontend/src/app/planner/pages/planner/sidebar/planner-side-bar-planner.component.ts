import { Component } from '@angular/core';
import { MatExpansionModule } from '@angular/material/expansion';
import { DocLinkComponent } from '@app/components/shared/link';
import { PlanComponent } from './plan.component';

@Component({
  selector: 'kpn-planner-sidebar-planner',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel [expanded]="true">
      <mat-expansion-panel-header>
        <mat-panel-title>
          <div class="header">
            <h1 i18n="@@planner.title">Route planner</h1>
            <kpn-doc-link [subject]="'planner'"></kpn-doc-link>
          </div>
        </mat-panel-title>
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <kpn-plan />
      </ng-template>
    </mat-expansion-panel>
  `,
  styles: `
    .header {
      display: flex;
      align-items: center;
      width: 100%;
    }

    .header h1 {
      flex: 1;
      display: inline-block;
    }
  `,
  standalone: true,
  imports: [MatExpansionModule, DocLinkComponent, PlanComponent],
})
export class PlannerSideBarPlannerComponent {}
