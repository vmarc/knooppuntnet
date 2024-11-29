import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatExpansionModule } from '@angular/material/expansion';
import { State } from '@app/state';

@Component({
  selector: 'kpn-explore-scope',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel [expanded]="expanded()" (expandedChange)="expandedChanged($event)">
      <mat-expansion-panel-header> Scope</mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <mat-checkbox [checked]="scopeInternational()" (change)="updateScopeInternational($event)">
          International
        </mat-checkbox>
        <mat-checkbox [checked]="scopeNational()" (change)="updateScopeNational($event)">
          National
        </mat-checkbox>
        <mat-checkbox [checked]="scopeRegional()" (change)="updateScopeRegional($event)">
          Regional
        </mat-checkbox>
        <mat-checkbox [checked]="scopeLocal()" (change)="updateScopeLocal($event)">
          Local
        </mat-checkbox>
        <mat-checkbox [checked]="scopeNodeRoutes()" (change)="updateScopeNodeRoutes($event)">
          Node routes
        </mat-checkbox>
      </ng-template>
    </mat-expansion-panel>
  `,
  styles: `
    mat-checkbox {
      display: block;
    }
  `,
  standalone: true,
  imports: [MatExpansionModule, MatCheckbox],
})
export class ExploreScopeComponent {
  private readonly state = inject(State);
  private readonly scopes = this.state.map.scopes;
  protected readonly scopeInternational = this.scopes.scopeInternational;
  protected readonly scopeNational = this.scopes.scopeNational;
  protected readonly scopeRegional = this.scopes.scopeRegional;
  protected readonly scopeLocal = this.scopes.scopeLocal;
  protected readonly scopeNodeRoutes = this.scopes.scopeNodeRoutes;

  expanded(): boolean {
    // TODO redesign - read preference
    return true;
  }

  expandedChanged(expanded: boolean): void {
    // TODO redesign - store preference
  }

  updateScopeInternational(event: MatCheckboxChange): void {
    this.scopes.updateScopeInternational(event.checked);
  }

  updateScopeNational(event: MatCheckboxChange): void {
    this.scopes.updateScopeNational(event.checked);
  }

  updateScopeRegional(event: MatCheckboxChange): void {
    this.scopes.updateScopeRegional(event.checked);
  }

  updateScopeLocal(event: MatCheckboxChange): void {
    this.scopes.updateScopeLocal(event.checked);
  }

  updateScopeNodeRoutes(event: MatCheckboxChange): void {
    this.scopes.updateScopeNodeRoutes(event.checked);
  }
}
