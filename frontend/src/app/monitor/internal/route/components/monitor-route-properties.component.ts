import { CdkStepper } from '@angular/cdk/stepper';
import { computed } from '@angular/core';
import { signal } from '@angular/core';
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
import { MonitorRouteForm } from '@app/monitor/internal/route/components/monitor-route-form.service';
import { Translations } from '@app/shared/i18n/translations';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzStepComponent } from 'ng-zorro-antd/steps';
import { NzStepsComponent } from 'ng-zorro-antd/steps';
import { MonitorRoutePropertiesStep1GroupComponent } from './monitor-route-properties-step-1-group.component';
import { MonitorRoutePropertiesStep2NameComponent } from './monitor-route-properties-step-2-name.component';
import { MonitorRoutePropertiesStep3RelationComponent } from './monitor-route-properties-step-3-relation.component';
import { MonitorRoutePropertiesStep4ReferenceTypeComponent } from './monitor-route-properties-step-4-reference-type.component';
import { MonitorRoutePropertiesStep5ReferenceDetailsComponent } from './monitor-route-properties-step-5-reference-details.component';
import { MonitorRoutePropertiesStep6CommentComponent } from './monitor-route-properties-step-6-comment.component';

@Component({
  selector: 'ui-monitor-route-properties',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <!-- eslint-disable @angular-eslint/template/cyclomatic-complexity -->
    <nz-steps [nzCurrent]="currentStep()" nzSize="small" (nzIndexChange)="stepChange($event)">
      @if (mode() === 'update') {
        <nz-step nzTitle="Group" i18n-nzTitle="@@monitor.route.properties.step.group" />
      }
      <nz-step nzTitle="Name" i18n-nzTitle="@@monitor.route.properties.step.name-description" />
      <nz-step nzTitle="Relation" i18n-nzTitle="@@monitor.route.properties.step.osm-relation" />
      <nz-step nzTitle="Reference" i18n-nzTitle="@@monitor.route.properties.step.reference-type" />
      <nz-step
        nzTitle="Reference details"
        i18n-nzTitle="@@monitor.route.properties.step.reference-details"
      />
      <nz-step nzTitle="Comment" i18n-nzTitle="@@monitor.route.properties.step.comment" />
    </nz-steps>

    @if (isCurrentStepGroup()) {
      <ui-monitor-route-properties-step-1-group [routeGroups]="routeGroups()" />
    }
    @if (isCurrentStepName()) {
      <ui-monitor-route-properties-step-2-name [mode]="mode()" />
    }
    @if (isCurrentStepRelation()) {
      <ui-monitor-route-properties-step-3-relation />
    }
    @if (isCurrentStepReference()) {
      <ui-monitor-route-properties-step-4-reference-type />
    }
    @if (isCurrentStepReferenceDetails()) {
      <ui-monitor-route-properties-step-5-reference-details />
    }
    @if (isCurrentStepReferenceComment()) {
      <ui-monitor-route-properties-step-6-comment />
    }

    @if (form.errors && form.errors['routeNameNonUnique']) {
      <p class="kpn-form-error" i18n="@@monitor.route.properties.name.unique">
        The route name should be unique within its the group. A route with name "{{ name.value }}"
        already exists within group "{{ group.value.groupName }}".
      </p>
    }

    <div class="kpn-button-group">
      <button
        nz-button
        nzType="primary"
        id="save"
        (click)="save()"
        [disabled]="form.invalid"
        i18n="@@action.save"
      >
        Save
      </button>
      <a [routerLink]="groupLink()" id="cancel">{{ cancelLinkText }}</a>
    </div>
  `,
  providers: [CdkStepper, MonitorRouteForm],
  imports: [
    MonitorRoutePropertiesStep1GroupComponent,
    MonitorRoutePropertiesStep2NameComponent,
    MonitorRoutePropertiesStep3RelationComponent,
    MonitorRoutePropertiesStep4ReferenceTypeComponent,
    MonitorRoutePropertiesStep5ReferenceDetailsComponent,
    MonitorRoutePropertiesStep6CommentComponent,
    NzButtonComponent,
    NzStepComponent,
    NzStepsComponent,
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

  protected readonly currentStep = signal<number>(0);

  protected readonly isCurrentStepGroup = computed(() => this.isCurrentStep(0));
  protected readonly isCurrentStepName = computed(() => this.isCurrentStep(1));
  protected readonly isCurrentStepRelation = computed(() => this.isCurrentStep(2));
  protected readonly isCurrentStepReference = computed(() => this.isCurrentStep(3));
  protected readonly isCurrentStepReferenceDetails = computed(() => this.isCurrentStep(4));
  protected readonly isCurrentStepReferenceComment = computed(() => this.isCurrentStep(5));

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

  stepChange(index: number): void {
    this.currentStep.set(index);
  }

  private isCurrentStep(index: number): boolean {
    const delta = this.mode() === 'update' ? 0 : 1;
    return this.currentStep() === index - delta;
  }

  save(): void {
    this.monitorForm.save();
  }
}
