import { NgClass } from '@angular/common';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MonitorRouteGroup } from '@api/common/monitor';
import { MonitorRouteProperties } from '@api/common/monitor';
import { MonitorRouteUpdate } from '@api/common/monitor/monitor-route-update';
import { MonitorWebsocketService } from '../../monitor-websocket.service';
import { MonitorRouteFormSaveComponent } from './monitor-route-form-save.component';
import { MonitorRoutePropertiesComponent } from './monitor-route-properties.component';

@Component({
  selector: 'kpn-monitor-route-form',
  template: `
    <div [ngClass]="{ hidden: saving }">
      <kpn-monitor-route-properties
        [mode]="mode()"
        [groupName]="groupName()"
        [initialProperties]="initialProperties()"
        [routeGroups]="routeGroups()"
        (update)="update($event)"
      ></kpn-monitor-route-properties>
    </div>
    <div [ngClass]="{ hidden: !saving }">
      <kpn-monitor-route-form-save [command]="command" />
    </div>
  `,
  styles: `
    .hidden {
      display: none;
    }
  `,
  providers: [MonitorWebsocketService],
  standalone: true,
  imports: [MonitorRouteFormSaveComponent, MonitorRoutePropertiesComponent, NgClass],
})
export class MonitorRouteFormComponent {
  mode = input.required<string>();
  groupName = input.required<string>();
  initialProperties = input.required<MonitorRouteProperties>();
  routeGroups = input<MonitorRouteGroup[]>([]);

  private readonly monitorWebsocketService = inject(MonitorWebsocketService);

  saving = false;
  command: MonitorRouteUpdate;

  update(command: MonitorRouteUpdate): void {
    this.monitorWebsocketService.reset();
    this.command = command;
    this.saving = true;
    this.monitorWebsocketService.sendCommand(command);
  }
}
