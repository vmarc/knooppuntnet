import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';

@Component({
  selector: 'ui-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-breadcrumb>
      @for (item of breadcrumbItems(); track $index) {
        <nz-breadcrumb-item>
          @if (item.routerLink) {
            <a [routerLink]="item.routerLink">{{ item.label }}</a>
          } @else {
            <span>{{ item.label }}</span>
          }
        </nz-breadcrumb-item>
      }
    </nz-breadcrumb>
  `,
  imports: [NzBreadCrumbComponent, NzBreadCrumbItemComponent, RouterLink],
})
export class BreadcrumbComponent {
  readonly breadcrumbItems = input.required<BreadcrumbItem[]>();
}
