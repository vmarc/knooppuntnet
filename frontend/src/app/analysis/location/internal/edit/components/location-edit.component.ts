import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { LocationEditPage } from '@api/common/location/location-edit-page';
import { EditConfiguration } from '@app/analysis/components/edit/edit-configuration';
import { EditParameters } from '@app/analysis/components/edit/edit-parameters';
import { EditService } from '@app/shared/components/edit.service';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzCheckboxComponent } from 'ng-zorro-antd/checkbox';

@Component({
  selector: 'ui-location-edit',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p>
      <label
        nz-checkbox
        [nzChecked]="nodeSelection"
        (nzCheckedChange)="nodeSelectionChanged($event)"
      >
        {{ page().summary.nodeCount }}
        <span i18n="@@location-edit.nodes">nodes (quick)</span>
      </label>
    </p>
    <p>
      <label
        nz-checkbox
        [nzChecked]="routeRelationsSelection"
        (nzCheckedChange)="routeRelationsSelectionChanged($event)"
      >
        {{ page().summary.routeCount }}
        <span i18n="@@location-edit.routes">routes relations (quick)</span>
      </label>
    </p>
    <p>
      <label
        nz-checkbox
        [nzChecked]="fullRouteSelection"
        (nzCheckedChange)="fullRouteSelectionChanged($event)"
      >
        {{ page().summary.routeCount }}
        <span i18n="@@location-edit.full-routes">routes with ways (takes more time)</span>
      </label>
    </p>
    @if (showEstimatedTime()) {
      <p>
        <i i18n="@@location-edit.time-warning">
          We estimate that it will take perhaps about {{ seconds }} seconds to load all nodes and
          routes in the editor.
        </i>
      </p>
    }
    <p>
      <button
        nz-button
        nzType="primary"
        (click)="edit()"
        title="Open in editor (like JOSM)"
        i18n-title="@@location-edit.submit.tooltip"
        i18n="@@location-edit.submit"
      >
        Load in JOSM editor
      </button>
    </p>
  `,
  imports: [NzButtonComponent, NzCheckboxComponent],
})
export class LocationEditComponent implements OnInit {
  readonly page = input.required<LocationEditPage>();

  private readonly editService = inject(EditService);

  seconds = 0;

  nodeSelection = true;
  routeRelationsSelection = true;
  fullRouteSelection = false;

  protected showEstimatedTime = signal<boolean>(false);

  private readonly configuration = new EditConfiguration();

  ngOnInit(): void {
    this.updateExpectation();
  }

  nodeSelectionChanged(checked: boolean) {
    this.nodeSelection = checked;
    this.updateExpectation();
  }

  routeRelationsSelectionChanged(checked: boolean) {
    this.routeRelationsSelection = checked;
    this.updateExpectation();
  }

  fullRouteSelectionChanged(checked: boolean) {
    this.fullRouteSelection = checked;
    this.updateExpectation();
  }

  edit(): void {
    const editParameters = this.buildEditParameters();
    this.editService.edit(editParameters);
  }

  private updateExpectation(): void {
    const parameters: EditParameters = this.buildEditParameters();
    this.seconds = this.configuration.seconds(parameters);
    this.showEstimatedTime.set(this.seconds > 3);
  }

  private buildEditParameters(): EditParameters {
    let editParameters: EditParameters = {
      bounds: this.page().bounds,
    };

    if (this.nodeSelection === true) {
      editParameters = {
        ...editParameters,
        nodeIds: this.page().nodeIds,
      };
    }

    if (this.routeRelationsSelection === true) {
      editParameters = {
        ...editParameters,
        relationIds: this.page().routeIds,
        fullRelation: this.fullRouteSelection,
      };
    }
    return editParameters;
  }
}
