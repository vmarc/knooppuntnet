import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteRelationInfo } from '@api/common/monitor/monitor-route-relation-info';
import { SegmentInfo } from '@api/common/route/segment-info';
import { RouteSegmentListPopupComponent } from '@app/shared/components/route/segments/route-segment-list-popup.component';
import { RouteSegmentPopupComponent } from '@app/shared/components/route/segments/route-segment-popup.component';
import { RouteSegmentRelationPopupComponent } from '@app/shared/components/route/segments/route-segment-relation-popup.component';
import { RouteSegmentListComponent } from '@app/shared/components/route/segments/route-segment-list.component';
import { SegmentPopupEvent } from '@app/shared/components/route/segments/segment-popup-event';
import { SegmentRelationPopupEvent } from '@app/shared/components/route/segments/segment-relation-popup-event';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzContextMenuService } from 'ng-zorro-antd/dropdown';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-route-segments',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button
      nz-button
      nzShape="circle"
      (click)="openSegmentListPopup($event, segmentListMenu)"
      class="kpn-small-spacer-above"
    >
      <nz-icon nzType="ellipsis" />
    </button>

    <ui-route-segment-list
      [segments]="segments()"
      [relations]="relations()"
      [selectedSegment]="selectedSegment()"
      (selectChange)="selectSegment($event)"
      (segmentPopup)="openSegmentPopup($event, segmentMenu)"
      (relationPopup)="openRelationPopup($event, relationMenu)"
    />

    <nz-dropdown-menu #segmentListMenu="nzDropdownMenu">
      <ui-route-segment-list-popup
        [showSegments]="showSegments()"
        (showSegmentsChange)="showSegmentsChanged($event)"
        (zoomToFitRoute)="zoomToFitRouteClicked()"
      />
    </nz-dropdown-menu>

    <nz-dropdown-menu #segmentMenu="nzDropdownMenu">
      <ui-route-segment-popup [segment]="selectedSegment()" />
    </nz-dropdown-menu>

    <nz-dropdown-menu #relationMenu="nzDropdownMenu">
      <ui-route-segment-relation-popup
        [segment]="selectedSegment()"
        [relationId]="selectedRelationId()"
      />
    </nz-dropdown-menu>
  `,
  imports: [
    NzButtonComponent,
    NzDropdownMenuComponent,
    NzIconDirective,
    RouteSegmentListComponent,
    RouteSegmentListPopupComponent,
    RouteSegmentPopupComponent,
    RouteSegmentRelationPopupComponent,
  ],
})
export class RouteSegmentsComponent {
  private readonly nzContextMenuService = inject(NzContextMenuService);

  readonly segments = input.required<SegmentInfo[]>();
  readonly relations = input.required<MonitorRouteRelationInfo[]>();
  readonly showSegments = input.required<boolean>();

  readonly selectChange = output<SegmentInfo>();
  readonly relationPopup = output<SegmentRelationPopupEvent>();
  readonly segmentPopup = output<SegmentPopupEvent>();
  readonly showSegmentsChange = output<boolean>();
  readonly zoomToFitRoute = output<void>();

  protected readonly selectedSegment = signal<SegmentInfo>(undefined);
  protected readonly selectedRelationId = signal<number>(undefined);

  selectSegment(segment: SegmentInfo): void {
    this.selectChange.emit(segment);
  }

  openSegmentListPopup(event: MouseEvent, menu: NzDropdownMenuComponent): void {
    event.stopPropagation();
    event.preventDefault();
    this.nzContextMenuService.create(event, menu);
  }

  openSegmentPopup(event: SegmentPopupEvent, menu: NzDropdownMenuComponent): void {
    event.event.stopPropagation();
    event.event.preventDefault();
    this.selectedSegment.set(event.segment);
    this.nzContextMenuService.create(event.event, menu);
  }

  openRelationPopup(event: SegmentRelationPopupEvent, menu: NzDropdownMenuComponent): void {
    event.event.stopPropagation();
    event.event.preventDefault();
    this.selectedSegment.set(event.segment);
    this.selectedRelationId.set(event.relationId);
    this.nzContextMenuService.create(event.event, menu);
  }

  showSegmentsChanged(value: boolean): void {
    this.showSegmentsChange.emit(value);
  }

  zoomToFitRouteClicked(): void {
    this.zoomToFitRoute.emit();
  }
}
