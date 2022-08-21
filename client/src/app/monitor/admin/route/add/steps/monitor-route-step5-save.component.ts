import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { MonitorRouteAdd } from '@api/common/monitor/monitor-route-add';
import { Store } from '@ngrx/store';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppState } from '../../../../../core/core.state';
import { MonitorService } from '../../../../monitor.service';
import { selectMonitorGroupName } from '../../../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-step-5-save',
  template: `
    <div class="kpn-button-group">
      <button mat-stroked-button matStepperPrevious>Back</button>
      <button mat-raised-button color="primary" matStepperNext (click)="save()">
        Save
      </button>
    </div>
  `,
})
export class MonitorRouteStep5SaveComponent {
  @Input() name: FormControl<string>;
  @Input() description: FormControl<string>;
  @Input() relationId: FormControl<string>;
  @Input() referenceType: FormControl<string | null>;
  @Input() referenceTimestamp: FormControl<string>;
  @Input() gpxFilename: FormControl<string>;
  @Input() gpxFile: FormControl<File>;
  @Input() form: FormGroup;

  constructor(
    private monitorService: MonitorService,
    private store: Store<AppState>
  ) {}

  save(): void {
    this.store
      .select(selectMonitorGroupName)
      .pipe(
        mergeMap((groupName) => {
          const add: MonitorRouteAdd = {
            name: this.name.value,
            description: this.description.value,
            relationId: this.relationId.value,
          };
          console.log('SAVE add=' + JSON.stringify(add));
          return this.monitorService.addRoute(groupName, add).pipe(
            map(() => {
              if (this.referenceType.value === 'gpx') {
                const file = this.gpxFile.value;
                return this.monitorService.routeGpxUpload(
                  groupName,
                  this.name.value,
                  file
                );
              }
              return of(true);
            })
          );
        })
      )
      .subscribe();
  }
}
