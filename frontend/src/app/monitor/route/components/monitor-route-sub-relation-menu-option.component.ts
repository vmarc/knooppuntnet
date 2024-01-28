import { NgClass } from '@angular/common';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MonitorRouteSubRelation } from '@api/common/monitor';

@Component({
  selector: 'kpn-monitor-sub-relation-menu-option',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      (click)="goto()"
      [ngClass]="{ disabled: !routeSubRelation() }"
      [title]="routeSubRelation()?.name"
    >
      {{ name() }}</a
    >
  `,
  styles: `
    .disabled {
      pointer-events: none;
      color: grey;
    }
  `,
  standalone: true,
  imports: [NgClass],
})
export class MonitorRouteSubRelationMenuOptionComponent {
  routeSubRelation = input.required<MonitorRouteSubRelation>();
  name = input.required<string>();
  @Output() selectSubRelation = new EventEmitter<MonitorRouteSubRelation>();

  goto(): void {
    this.selectSubRelation.emit(this.routeSubRelation());
  }
}
