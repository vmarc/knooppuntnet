import { output } from '@angular/core';
import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MonitorAction } from '@api/common/monitor/monitor-action';
import { MonitorRouteGroup } from '@api/common/monitor/monitor-route-group';
import { MonitorRouteProperties } from '@api/common/monitor/monitor-route-properties';
import { MonitorRouteUpdate } from '@api/common/monitor/monitor-route-update';
import { MonitorRouteForm } from './monitor-route-form.service';
import { Translations } from '@app/shared/i18n/translations';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzDividerComponent } from 'ng-zorro-antd/divider';
import { NzFormDirective } from 'ng-zorro-antd/form';
import { MonitorRoutePropertiesStep2NameComponent } from './monitor-route-properties-step-2-name.component';
import { MonitorRoutePropertiesStep3RelationComponent } from './monitor-route-properties-step-3-relation.component';
import { MonitorRoutePropertiesStep4ReferenceTypeComponent } from './monitor-route-properties-step-4-reference-type.component';
import { MonitorRoutePropertiesStep5ReferenceDetailsComponent } from './monitor-route-properties-step-5-reference-details.component';
import { MonitorRoutePropertiesStep6CommentComponent } from './monitor-route-properties-step-6-comment.component';

@Component({
  selector: 'ui-monitor-route-properties',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <form nz-form nzLayout="vertical" [formGroup]="form" (ngSubmit)="save()">
      <div>
        <ui-monitor-route-properties-step-2-name [mode]="mode()" [routeGroups]="routeGroups()" />
        <nz-divider />
        <ui-monitor-route-properties-step-3-relation />
        <nz-divider />
        <ui-monitor-route-properties-step-4-reference-type />
        <nz-divider />
        <ui-monitor-route-properties-step-5-reference-details />
        <nz-divider />
        <ui-monitor-route-properties-step-6-comment />
        <nz-divider />
      </div>

      @if (form.errors && form.errors['routeNameNonUnique']) {
        <p class="ant-form-item-explain-error" i18n="@@monitor.route.properties.name.unique">
          The route name should be unique within its the group. A route with name "{{ name.value }}"
          already exists within group "{{ group.value.groupName }}".
        </p>
      }

      <div class="kpn-button-group">
        <button nz-button nzType="primary" id="save" type="submit" i18n="@@action.save">
          Save
        </button>
        <a [routerLink]="groupLink()" id="cancel">{{ cancelLinkText }}</a>
      </div>
    </form>
  `,
  providers: [MonitorRouteForm],
  imports: [
    MonitorRoutePropertiesStep2NameComponent,
    MonitorRoutePropertiesStep3RelationComponent,
    MonitorRoutePropertiesStep4ReferenceTypeComponent,
    MonitorRoutePropertiesStep5ReferenceDetailsComponent,
    MonitorRoutePropertiesStep6CommentComponent,
    NzButtonComponent,
    NzDividerComponent,
    NzFormDirective,
    ReactiveFormsModule,
    RouterLink,
  ],
})
export class MonitorRoutePropertiesComponent implements OnInit, OnDestroy {
  readonly mode = input.required<MonitorAction>();
  readonly groupName = input.required<string>();
  readonly initialProperties = input.required<MonitorRouteProperties>();
  readonly routeGroups = input.required<MonitorRouteGroup[]>();
  readonly update = output<MonitorRouteUpdate>();

  private readonly monitorForm = inject(MonitorRouteForm);
  protected readonly form = this.monitorForm.form;
  protected readonly name = this.monitorForm.name;
  protected readonly group = this.monitorForm.group;

  protected readonly cancelLinkText = Translations.get('action.cancel');

  ngOnInit(): void {
    this.monitorForm.init(
      this.mode(),
      this.groupName(),
      this.initialProperties(),
      this.routeGroups()
    );
  }

  ngOnDestroy(): void {
    this.monitorForm.destroy();
  }

  groupLink(): string {
    return `/monitor/groups/${this.groupName()}`;
  }

  save(): void {
    this.monitorForm.save();
  }
}
