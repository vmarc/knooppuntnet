import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';

@Component({
  selector: 'ui-monitor-group-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" /> `,
  imports: [BreadcrumbComponent],
})
export class MonitorGroupBreadcrumbComponent {
  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.monitor,
    { label: Breadcrumbs.monitorGroupLabel },
  ];
}
