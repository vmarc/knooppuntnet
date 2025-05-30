import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SidebarComponent } from './sidebar.component';

@Component({
  selector: 'ui-analysis-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '<ui-sidebar/>',
  imports: [SidebarComponent],
})
export class AnalysisSidebarComponent {}
