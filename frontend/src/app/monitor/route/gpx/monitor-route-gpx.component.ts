import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { ValidationErrors } from '@angular/forms';
import { ValidatorFn } from '@angular/forms';
import { Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { TimestampUtil } from '@app/components/shared';
import { NavService } from '@app/components/shared';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { Translations } from '@app/i18n';
import { MonitorWebsocketService } from '../../monitor-websocket.service';
import { MonitorRouteFormErrorsComponent } from '../components/monitor-route-form-errors.component';
import { MonitorRouteFormSaveStepComponent } from '../components/monitor-route-form-save-step.component';
import { MonitorRouteGpxBreadcrumbComponent } from './monitor-route-gpx-breadcrumb.component';
import { MonitorRouteGpxReferenceComponent } from './monitor-route-gpx-reference.component';
import { MonitorRouteGpxService } from './monitor-route-gpx.service';

@Component({
  selector: 'kpn-monitor-route-gpx',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (_state(); as state) {
      <kpn-page>
        <kpn-monitor-route-gpx-breadcrumb
          [groupName]="state.groupName"
          [groupLink]="state.groupLink"
          [routeName]="state.routeName"
          [routeLink]="state.routeLink"
        />

        @if (state.response; as response) {
          @if (!response.result) {
            <div i18n="@@monitor.route.gpx.not-found" class="kpn-error">Route not found</div>
          }

          @if (response.result; as page) {
            <h1>{{ page.subRelationDescription }}</h1>
            <h2>GPX reference</h2>

            <div class="gpx-form">
              <form [formGroup]="form" #ngForm="ngForm">
                <kpn-monitor-route-gpx-reference
                  [ngForm]="ngForm"
                  [gpxReferenceDate]="gpxReferenceDate"
                  [referenceFilename]="referenceFilename"
                  [referenceFile]="referenceFile"
                />
              </form>
            </div>

            @if (busy() === false) {
              <div class="kpn-button-group">
                <button
                  mat-raised-button
                  id="save"
                  color="primary"
                  (click)="save()"
                  [disabled]="form.invalid"
                  i18n="@@action.upload"
                >
                  Upload
                </button>
                <a [routerLink]="state.routeLink" id="cancel">{{ cancelLinkText }}</a>
              </div>
            }
          }
        }

        @if (busy() === true) {
          @for (step of steps(); track $index) {
            <kpn-monitor-route-form-save-step [step]="step" />
          }

          <kpn-monitor-route-form-errors [errors]="errors()" />

          <div class="kpn-button-group">
            <button
              mat-stroked-button
              id="goto-analysis-result-button"
              [routerLink]="state.routeLink"
              [disabled]="done() === false"
              i18n="@@monitor.route.gpx-delete.action.analysis-result"
            >
              Back to route details
            </button>
          </div>
        }

        <kpn-sidebar sidebar />
      </kpn-page>
    }
  `,
  styles: `
    .gpx-form {
      margin-top: 2rem;
      margin-left: 2rem;
      margin-bottom: 4rem;
    }
  `,
  providers: [MonitorRouteGpxService, MonitorWebsocketService, NavService],
  standalone: true,
  imports: [
    MatButtonModule,
    MonitorRouteGpxBreadcrumbComponent,
    MonitorRouteGpxReferenceComponent,
    PageComponent,
    ReactiveFormsModule,
    RouterLink,
    SidebarComponent,
    MonitorRouteFormErrorsComponent,
    MonitorRouteFormSaveStepComponent,
  ],
})
export class MonitorRouteGpxComponent {
  private readonly service = inject(MonitorRouteGpxService);
  private readonly monitorWebsocketService = inject(MonitorWebsocketService);
  protected readonly cancelLinkText = Translations.get('@@action.cancel');

  protected readonly _state = this.service.state;

  protected readonly steps = this.monitorWebsocketService.steps;
  protected readonly errors = this.monitorWebsocketService.errors;
  protected readonly busy = this.monitorWebsocketService.busy;
  protected readonly done = this.monitorWebsocketService.done;

  protected readonly gpxReferenceDate = new FormControl<Date>(null, Validators.required);
  protected readonly referenceFilename = new FormControl<string>(null, Validators.required);
  protected readonly referenceFile = new FormControl<File>(null, this.referenceFileValidator());

  protected readonly form = new FormGroup({
    gpxReferenceDate: this.gpxReferenceDate,
    referenceFilename: this.referenceFilename,
    referenceFile: this.referenceFile,
  });

  save(): void {
    const referenceTimestamp = TimestampUtil.toTimestamp(this.gpxReferenceDate.value);
    this.service.save(this.referenceFile.value, referenceTimestamp);
  }

  private referenceFileValidator(): ValidatorFn {
    return (control: AbstractControl<File>): ValidationErrors | null => {
      const maxFileSizeMb = 20;
      const maxFileSize = maxFileSizeMb * 1024 * 1024;
      if (control.value) {
        if (control.value.size > maxFileSize) {
          return { maxFileSizeExceeded: `${maxFileSizeMb}Mb` };
        }
      }
      return null;
    };
  }
}
