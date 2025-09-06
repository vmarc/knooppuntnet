import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocalLocationNode } from '@app/analysis/location/internal/selection/components/local-location-node';
import { LocationPipe } from '@app/shared/components/format/location.pipe';
import { ZeroIntegerFormatPipe } from '@app/shared/components/format/zero-integer-format.pipe';

@Component({
  selector: 'ui-location-tree-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (node(); as node) {
      <div>
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
  imports: [LocationPipe, ZeroIntegerFormatPipe],
})
export class LocationTreeNodeComponent {
  readonly node = input.required<LocalLocationNode>();

  readonly selection = output<string>();

  select(expandableNode: LocalLocationNode): void {
    const locationName =
      expandableNode.path.length > 0
        ? expandableNode.path + ':' + expandableNode.name
        : expandableNode.name;

    this.selection.emit(locationName);
  }
}
