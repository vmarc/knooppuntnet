import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SidebarComponent } from './sidebar.component';

@Component({
  selector: 'kpn-analysis-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '<kpn-sidebar/>',
  imports: [SidebarComponent],
})
export class AnalysisSidebarComponent {}
