import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { MenuOption } from '@app/shared/components/menu/menu-option';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { NodeService } from '../node.service';

@Component({
  selector: 'ui-node-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
    <ui-page-header [pageTitle]="nodeName()" subject="node-page">
      <span i18n="@@node.title">Node</span>
      @if (nodeName()) {
        <span>&nbsp;{{ nodeName() }}</span>
      } @else {
        <span>&nbsp;{{ nodeId() }}</span>
      }
    </ui-page-header>

    <ui-page-menu [pageName]="pageName()" [options]="menuOptions()" />
  `,
  imports: [PageHeaderComponent, PageMenuComponent, BreadcrumbComponent],
})
export class NodePageHeaderComponent {
  private readonly service = inject(NodeService);
  protected readonly pageName = computed(() => this.service.pageName());
  protected readonly nodeName = computed(() => this.service.nodeName());
  protected readonly nodeId = computed(() => this.service.nodeId());

  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    { routerLink: '/', label: $localize`:@@breadcrumb.home:Home` },
    { routerLink: '/analysis', label: $localize`:@@breadcrumb.analysis:Analysis` },
    { label: $localize`:@@breadcrumb.node:Node` },
  ];

  protected readonly menuOptions: Signal<MenuOption[]> = computed(() => {
    const link = `/analysis/node/${this.service.nodeId()}`;
    return [
      {
        pageName: 'details',
        pageLink: link,
        label: $localize`:@@node.menu.details:Details`,
      },
      {
        pageName: 'changes',
        pageLink: link + '/changes',
        label: $localize`:@@node.menu.changes:Changes`,
        elementCount: this.service.changeCount(),
      },
    ];
  });
}
