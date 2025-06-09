import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocationChangesTreeNode } from '@api/common/location-changes-tree-node';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { IconInvestigateComponent } from '@app/shared/components/icon/icon-investigate.component';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';

@Component({
  selector: 'ui-change-set-location-tree-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    @for (tree of trees(); track tree) {
      <div>
        <div class="kpn-line location-block">
          <span>{{ tree.locationName }}</span>
          @if (tree.happy) {
            <ui-icon-happy />
          }
          @if (tree.investigate) {
            <ui-icon-investigate />
          }
        </div>
        <div>
          @if (tree.routeChanges.removed.length > 0) {
            <div class="refs-block">
              <div>Removed routes ({{ tree.routeChanges.removed.length }})</div>
              @for (ref of tree.routeChanges.removed; track ref) {
                <div class="refs">
                  <div class="kpn-line">
                    <ui-link-route [routeId]="ref.id" [routeName]="ref.name" />
                    @if (ref.happy) {
                      <ui-icon-happy />
                    }
                    @if (ref.investigate) {
                      <ui-icon-investigate />
                    }
                  </div>
                </div>
              }
            </div>
          }
          @if (tree.routeChanges.added.length > 0) {
            <div class="refs-block">
              <div>Added routes ({{ tree.routeChanges.added.length }})</div>
              @for (ref of tree.routeChanges.added; track ref) {
                <div class="refs">
                  <div class="kpn-line">
                    <ui-link-route [routeId]="ref.id" [routeName]="ref.name" />
                    @if (ref.happy) {
                      <ui-icon-happy />
                    }
                    @if (ref.investigate) {
                      <ui-icon-investigate />
                    }
                  </div>
                </div>
              }
            </div>
          }
          @if (tree.routeChanges.updated.length > 0) {
            <div class="refs-block">
              <div>Updated routes ({{ tree.routeChanges.updated.length }})</div>
              @for (ref of tree.routeChanges.updated; track ref) {
                <div class="refs">
                  <div class="kpn-line">
                    <ui-link-route [routeId]="ref.id" [routeName]="ref.name" />
                    @if (ref.happy) {
                      <ui-icon-happy />
                    }
                    @if (ref.investigate) {
                      <ui-icon-investigate />
                    }
                  </div>
                </div>
              }
            </div>
          }
          @if (tree.nodeChanges.removed.length > 0) {
            <div class="refs-block">
              <div>Updated nodes ({{ tree.nodeChanges.removed.length }})</div>
              @for (ref of tree.nodeChanges.removed; track ref) {
                <div class="refs">
                  <div class="kpn-line">
                    <ui-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
                    @if (ref.happy) {
                      <ui-icon-happy />
                    }
                    @if (ref.investigate) {
                      <ui-icon-investigate />
                    }
                  </div>
                </div>
              }
            </div>
          }
          @if (tree.nodeChanges.added.length > 0) {
            <div class="refs-block">
              <div>Updated nodes ({{ tree.nodeChanges.added.length }})</div>
              @for (ref of tree.nodeChanges.added; track ref) {
                <div class="refs">
                  <div class="kpn-line">
                    <ui-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
                    @if (ref.happy) {
                      <ui-icon-happy />
                    }
                    @if (ref.investigate) {
                      <ui-icon-investigate />
                    }
                  </div>
                </div>
              }
            </div>
          }
          @if (tree.nodeChanges.updated.length > 0) {
            <div class="refs-block">
              <div>Updated nodes ({{ tree.nodeChanges.updated.length }})</div>
              @for (ref of tree.nodeChanges.updated; track ref) {
                <div class="refs">
                  <div class="kpn-line">
                    <ui-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
                    @if (ref.happy) {
                      <ui-icon-happy />
                    }
                    @if (ref.investigate) {
                      <ui-icon-investigate />
                    }
                  </div>
                </div>
              }
            </div>
          }
        </div>
        <div class="children">
          <ui-change-set-location-tree-node [trees]="tree.children" />
        </div>
      </div>
    }
  `,
  styles: `
    .children {
      margin-left: 1em;
    }

    .location-block {
      margin-top: 0.6em;
    }

    .refs-block {
      margin-top: 0.6em;
      margin-left: 1em;
    }

    .refs {
      margin-top: 0.3em;
      margin-left: 2em;
    }
  `,
  imports: [IconHappyComponent, IconInvestigateComponent, LinkNodeComponent, LinkRouteComponent],
})
export class ChangeSetLocationTreeNodeComponent {
  readonly trees = input.required<LocationChangesTreeNode[]>();
}
