import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-analysis-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-analysis-mode></kpn-analysis-mode>
    </kpn-sidebar>
  `,
})
export class AnalysisSidebarComponent {}
