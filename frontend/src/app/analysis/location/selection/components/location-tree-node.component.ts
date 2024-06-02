import { DecimalPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { input } from '@angular/core';
import { IntegerFormatPipe } from '@app/components/shared/format';
import { LocationPipe } from '../../../../shared/components/shared/format/location.pipe';
import { ZeroIntegerFormatPipe } from '../../../../shared/components/shared/format/zero-integer-format.pipe';
import { LocationFlatNode } from './location-flat-node';

@Component({
  selector: 'kpn-location-tree-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (node(); as node) {
      <div class="node">
        <a (click)="select(node)" class="link">{{ node.name | location }}</a>
        <span class="counts kpn-comma-list">
          <span class="kpn-space-separated">
            <span>{{ node.nodeCount | zeroInteger }}</span>
            <span i18n="@@location.tree.nodes">nodes</span>
          </span>
          <span class="kpn-space-separated">
            <span>{{ node.routeCount | zeroInteger }}</span>
            <span i18n="@@location.tree.routes">routes</span>
          </span>
          <span class="kpn-space-separated">
            <span>{{ node.factCount | zeroInteger }}</span>
            <span i18n="@@location.tree.facts">facts</span>
          </span>
        </span>
      </div>
    }
  `,
  styles: `
    .node {
      padding-top: 6px;
    }

    @media (max-width: 960px) {
      .link {
        display: block;
        padding-bottom: 6px;
      }

      .counts {
        padding-bottom: 18px;
      }
    }

    @media (min-width: 961px) {
      .link {
        display: inline-block;
      }

      .counts {
        padding-left: 20px;
      }
    }
  `,
  standalone: true,
  imports: [LocationPipe, DecimalPipe, IntegerFormatPipe, ZeroIntegerFormatPipe],
})
export class LocationTreeNodeComponent {
  node = input.required<LocationFlatNode>();

  @Output() selection = new EventEmitter<string>();

  select(expandableNode: LocationFlatNode): void {
    const locationName =
      expandableNode.path.length > 0
        ? expandableNode.path + ':' + expandableNode.name
        : expandableNode.name;

    this.selection.emit(locationName);
  }
}
