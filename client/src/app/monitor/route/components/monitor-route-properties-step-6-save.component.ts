import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MonitorRouteGroup } from '@api/common/monitor/monitor-route-group';
import { MonitorRouteProperties } from '@api/common/monitor/monitor-route-properties';
import { Store } from '@ngrx/store';
import { DayUtil } from '../../../components/shared/day-util';
import { AppState } from '../../../core/core.state';
import { MonitorService } from '../../monitor.service';
import { MonitorRouteParameters } from './monitor-route-parameters';
import { MonitorRouteSaveDialogComponent } from './monitor-route-save-dialog.component';

@Component({
  selector: 'kpn-monitor-route-properties-step-6-save',
  template: `
    <div class="kpn-button-group">
      <button mat-stroked-button matStepperPrevious>Back</button>
      <button mat-raised-button color="primary" matStepperNext (click)="save()">
        Save
      </button>
    </div>
  `,
})
export class MonitorRoutePropertiesStep6SaveComponent {
  @Input() initialProperties: MonitorRouteProperties;
  @Input() mode: string;
  @Input() group: FormControl<MonitorRouteGroup | null>;
  @Input() name: FormControl<string>;
  @Input() description: FormControl<string>;
  @Input() relationId: FormControl<string>;
  @Input() referenceType: FormControl<string | null>;
  @Input() osmReferenceDate: FormControl<Date | null>;
  @Input() gpxFilename: FormControl<string>;
  @Input() gpxFile: FormControl<File>;
  @Input() form: FormGroup;

  constructor(
    private monitorService: MonitorService,
    private store: Store<AppState>,
    private router: Router,
    private dialog: MatDialog
  ) {}

  save(): void {
    const data: MonitorRouteParameters = {
      mode: this.mode,
      initialProperties: this.initialProperties,
      properties: this.buildProperties(),
      gpxFile: this.gpxFile.value,
    };

    this.dialog.open(MonitorRouteSaveDialogComponent, {
      data,
      maxWidth: 600,
    });
  }

  private buildProperties(): MonitorRouteProperties {
    return {
      name: this.name.value,
      description: this.description.value,
      groupName: this.group.value?.groupName,
      relationId: this.relationId.value,
      referenceType: this.referenceType.value,
      osmReferenceDay: DayUtil.toDay(this.osmReferenceDate.value),
      gpxFileChanged: !!this.gpxFile.value,
      gpxFilename: this.gpxFilename.value,
    };
  }
}
