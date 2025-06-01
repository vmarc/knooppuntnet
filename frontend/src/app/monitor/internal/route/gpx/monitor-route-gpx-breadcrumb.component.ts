import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';

@Component({
  selector: 'ui-monitor-route-gpx-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <ui-breadcrumb [breadcrumbItems]="breadcrumbItems()" /> `,
  imports: [BreadcrumbComponent],
})
export class MonitorRouteGpxBreadcrumbComponent {
  readonly groupName = input.required<string>();
  readonly routeName = input.required<string>();
  readonly groupLink = input.required<string>();
  readonly routeLink = input.required<string>();
  protected readonly breadcrumbItems: Signal<BreadcrumbItem[]> = computed(() => {
    return [
      Breadcrumbs.home,
      Breadcrumbs.monitor,
      { routerLink: this.groupLink(), label: this.groupName() },
      { routerLink: this.routeLink(), label: this.routeName() },
      { label: Breadcrumbs.monitorRouteGpxLabel },
    ];
  });
}
