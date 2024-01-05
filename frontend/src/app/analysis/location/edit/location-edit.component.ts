import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { LocationEditPage } from '@api/common/location';
import { EditConfiguration } from '@app/analysis/components/edit';
import { EditParameters } from '@app/analysis/components/edit';
import { EditService } from '@app/components/shared';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'kpn-location-edit',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p>
      <mat-checkbox [checked]="nodeSelection" (change)="nodeSelectionChanged($event)">
        {{ page.summary.nodeCount }}
        <span i18n="@@location-edit.nodes">nodes (quick)</span>
      </mat-checkbox>
    </p>
    <p>
      <mat-checkbox
        [checked]="routeRelationsSelection"
        (change)="routeRelationsSelectionChanged($event)"
      >
        {{ page.summary.routeCount }}
        <span i18n="@@location-edit.routes">routes relations (quick)</span>
      </mat-checkbox>
    </p>
    <p>
      <mat-checkbox [checked]="fullRouteSelection" (change)="fullRouteSelectionChanged($event)">
        {{ page.summary.routeCount }}
        <span i18n="@@location-edit.full-routes">routes with ways (takes more time)</span>
      </mat-checkbox>
    </p>
    @if (showEstimatedTime$ | async) {
      <p>
        <i i18n="@@location-edit.time-warning">
          We estimate that it will take perhaps about {{ seconds }} seconds to load all nodes and
          routes in the editor.
        </i>
      </p>
    }
    <p>
      <button
        mat-raised-button
        color="primary"
        (click)="edit()"
        title="Open in editor (like JOSM)"
        i18n-title="@@location-edit.submit.tooltip"
        i18n="@@location-edit.submit"
      >
        Load in JOSM editor
      </button>
    </p>
  `,
  standalone: true,
  imports: [MatCheckboxModule, MatButtonModule, AsyncPipe],
})
export class LocationEditComponent implements OnInit {
  @Input() page: LocationEditPage;

  private readonly editService = inject(EditService);

  seconds = 0;

  nodeSelection = true;
  routeRelationsSelection = true;
  fullRouteSelection = false;

  showEstimatedTime$ = new BehaviorSubject<boolean>(false);

  private readonly configuration = new EditConfiguration();

  ngOnInit(): void {
    this.updateExpectation();
  }

  nodeSelectionChanged(event: MatCheckboxChange) {
    this.nodeSelection = event.checked;
    this.updateExpectation();
  }

  routeRelationsSelectionChanged(event: MatCheckboxChange) {
    this.routeRelationsSelection = event.checked;
    this.updateExpectation();
  }

  fullRouteSelectionChanged(event: MatCheckboxChange) {
    this.fullRouteSelection = event.checked;
    this.updateExpectation();
  }

  edit(): void {
    const editParameters = this.buildEditParameters();
    this.editService.edit(editParameters);
  }

  private updateExpectation(): void {
    const parameters: EditParameters = this.buildEditParameters();
    this.seconds = this.configuration.seconds(parameters);
    this.showEstimatedTime$.next(this.seconds > 3);
  }

  private buildEditParameters(): EditParameters {
    let editParameters: EditParameters = {
      bounds: this.page.bounds,
    };

    if (this.nodeSelection === true) {
      editParameters = {
        ...editParameters,
        nodeIds: this.page.nodeIds,
      };
    }

    if (this.routeRelationsSelection === true) {
      editParameters = {
        ...editParameters,
        relationIds: this.page.routeIds,
        fullRelation: this.fullRouteSelection,
      };
    }
    return editParameters;
  }
}
