import { viewChild } from '@angular/core';
import { computed } from '@angular/core';
import { output } from '@angular/core';
import { signal } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Country } from '@api/common/country';
import { RouteType } from '@api/common/route-type';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzRadioGroupComponent } from 'ng-zorro-antd/radio';
import { NzRadioComponent } from 'ng-zorro-antd/radio';
import { NzTreeNode } from 'ng-zorro-antd/tree';
import { NzFormatEmitEvent, NzTreeComponent } from 'ng-zorro-antd/tree';
import { LocalLocationNode } from './local-location-node';
import { LocationTreeNodeComponent } from './location-tree-node.component';

@Component({
  selector: 'ui-location-tree',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-small-spacer-above kpn-small-spacer-below">
      @if (country() !== 'fr') {
        <button
          nz-button
          class="location-button"
          (click)="expandAll()"
          i18n="@@location.tree.expand-all"
        >
          Expand all
        </button>
        <button
          nz-button
          class="location-button"
          (click)="collapseAll()"
          i18n="@@location.tree.collapse-all"
        >
          Collapse all
        </button>
      }

      <nz-radio-group [ngModel]="all()" (ngModelChange)="allChanged($event)">
        <label nz-radio [nzValue]="true" i18n="@@location.tree.all">All</label>
        <label nz-radio [nzValue]="false" i18n="@@location.tree.in-use-only">In use only</label>
      </nz-radio-group>
    </div>

    @if (routeType() === 'hiking' && country() === 'fr') {
      <div class="kpn-small-spacer-below">
        <a routerLink="/analysis/hiking/fr/Parc du Vercors/details">Parc du Vercors</a>
      </div>
    }

    <nz-tree
      [nzData]="nodes()"
      (nzClick)="activeNode($event)"
      (nzDblClick)="openFolder($event)"
      [nzTreeTemplate]="nzTreeTemplate"
    />
    <ng-template #nzTreeTemplate let-origin="origin">
      <ui-location-tree-node [node]="origin" (selection)="selection.emit($event)" />
    </ng-template>
  `,
  styles: `
    .location-button {
      margin-right: 10px;
    }
  `,
  imports: [
    FormsModule,
    LocationTreeNodeComponent,
    NzButtonComponent,
    NzRadioComponent,
    NzRadioGroupComponent,
    NzTreeComponent,
    RouterLink,
  ],
})
export class LocationTreeComponent {
  readonly routeType = input.required<RouteType>();
  readonly country = input.required<Country>();
  readonly locationNode = input.required<LocalLocationNode>();

  readonly nzTree = viewChild(NzTreeComponent);

  readonly selection = output<string>();

  readonly all = signal<boolean>(false);

  readonly nodes = computed(() => [this.locationNode()]);

  expandAll(): void {
    this.expand(this.nzTree().getTreeNodes());
  }

  collapseAll(): void {
    this.collapse(this.nzTree().getTreeNodes());
  }

  private collapse(nodes: NzTreeNode[]): void {
    nodes.forEach((node) => {
      node.isExpanded = false;
      this.collapse(node.children);
    });
  }

  private expand(nodes: NzTreeNode[]): void {
    nodes.forEach((node) => {
      node.isExpanded = true;
      this.expand(node.children);
    });
  }

  allChanged(e): void {
    this.all.set(!this.all());
  }

  openFolder(data: NzTreeNode | NzFormatEmitEvent): void {
    if (data instanceof NzTreeNode) {
      data.isExpanded = !data.isExpanded;
    } else {
      const node = data.node;
      if (node) {
        node.isExpanded = !node.isExpanded;
      }
    }
  }

  activeNode(data: NzFormatEmitEvent): void {
    console.log('tree node clicked', data);
  }
}
