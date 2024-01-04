import { forwardRef } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { LocationChangesTreeNode } from '@api/common';
import { IconHappyComponent } from '@app/components/shared/icon';
import { IconInvestigateComponent } from '@app/components/shared/icon';
import { LinkNodeComponent } from '@app/components/shared/link';
import { LinkRouteComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-change-set-location-tree-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    @for (tree of trees; track tree) {
      <div>
        <div class="kpn-line location-block">
          <span>{{ tree.locationName }}</span>
          @if (tree.happy) {
            <kpn-icon-happy />
          }
          @if (tree.investigate) {
            <kpn-icon-investigate />
          }
        </div>
        <div>
          @if (tree.routeChanges.removed.length > 0) {
            <div class="refs-block">
              <div>Removed routes ({{ tree.routeChanges.removed.length }})</div>
              @for (ref of tree.routeChanges.removed; track ref) {
                <div class="refs">
                  <div class="kpn-line">
                    <kpn-link-route [routeId]="ref.id" [routeName]="ref.name" />
                    @if (ref.happy) {
                      <kpn-icon-happy />
                    }
                    @if (ref.investigate) {
                      <kpn-icon-investigate />
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
                    <kpn-link-route [routeId]="ref.id" [routeName]="ref.name" />
                    @if (ref.happy) {
                      <kpn-icon-happy />
                    }
                    @if (ref.investigate) {
                      <kpn-icon-investigate />
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
                    <kpn-link-route [routeId]="ref.id" [routeName]="ref.name" />
                    @if (ref.happy) {
                      <kpn-icon-happy />
                    }
                    @if (ref.investigate) {
                      <kpn-icon-investigate />
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
                    <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
                    @if (ref.happy) {
                      <kpn-icon-happy />
                    }
                    @if (ref.investigate) {
                      <kpn-icon-investigate />
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
                    <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
                    @if (ref.happy) {
                      <kpn-icon-happy />
                    }
                    @if (ref.investigate) {
                      <kpn-icon-investigate />
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
                    <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
                    @if (ref.happy) {
                      <kpn-icon-happy />
                    }
                    @if (ref.investigate) {
                      <kpn-icon-investigate />
                    }
                  </div>
                </div>
              }
            </div>
          }
        </div>
        <div class="children">
          <kpn-change-set-location-tree-node [trees]="tree.children" />
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
  standalone: true,
  imports: [
    IconHappyComponent,
    IconInvestigateComponent,
    LinkNodeComponent,
    LinkRouteComponent,
    forwardRef(() => ChangeSetLocationTreeNodeComponent),
  ],
})
export class ChangeSetLocationTreeNodeComponent {
  @Input() trees: LocationChangesTreeNode[];
}
