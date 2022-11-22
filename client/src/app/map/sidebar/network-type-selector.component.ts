import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatButtonToggleChange } from '@angular/material/button-toggle';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'kpn-network-type-selector',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-button-toggle-group
      [value]="networkType$ | async"
      (change)="networkTypeChanged($event)"
    >
      <mat-button-toggle value="cycling">
        <mat-icon svgIcon="cycling"></mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="hiking">
        <mat-icon svgIcon="hiking"></mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="horse-riding">
        <mat-icon svgIcon="horse-riding"></mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="motorboat">
        <mat-icon svgIcon="motorboat"></mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="canoe">
        <mat-icon svgIcon="canoe"></mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="inline-skating">
        <mat-icon svgIcon="inline-skating"></mat-icon>
      </mat-button-toggle>
    </mat-button-toggle-group>
  `,
  styles: [
    `
      :host ::ng-deep .mat-button-toggle > .mat-button-toggle-button {
        width: 34px;
        height: 34px;
      }

      :host
        ::ng-deep
        .mat-button-toggle
        > .mat-button-toggle-button
        > .mat-button-toggle-label-content {
        line-height: 34px;
        color: rgba(0, 0, 0, 0.8);
        padding: 0;
      }
    `,
  ],
})
export class NetworkTypeSelectorComponent implements OnInit {
  networkType$: Observable<string>;

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.networkType$ = this.activatedRoute.params.pipe(
      map((params) => params['networkType'])
    );
  }

  networkTypeChanged(event: MatButtonToggleChange) {
    this.router.navigate(['../' + event.value], {
      relativeTo: this.activatedRoute,
    });
  }
}
