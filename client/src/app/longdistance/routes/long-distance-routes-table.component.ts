import {ChangeDetectionStrategy} from '@angular/core';
import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {PageWidthService} from '../../components/shared/page-width.service';

export interface LdRoute {
  readonly group: string,
  readonly name: string,
  readonly relationId: number
}

@Component({
  selector: 'kpn-long-distance-routes-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <table mat-table [dataSource]="dataSource">

      <ng-container matColumnDef="group">
        <th mat-header-cell *matHeaderCellDef>Group</th>
        <td mat-cell *matCellDef="let route">
          {{route.group}}
        </td>
      </ng-container>

      <ng-container matColumnDef="name">
        <th mat-header-cell *matHeaderCellDef>Name</th>
        <td mat-cell *matCellDef="let route">
          {{route.name}}
        </td>
      </ng-container>

      <ng-container matColumnDef="relation">
        <th mat-header-cell *matHeaderCellDef>Relation</th>
        <td mat-cell *matCellDef="let route">
          <kpn-osm-link-relation [relationId]="route.relationId"></kpn-osm-link-relation>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns$ | async"></tr>
      <tr mat-row *matRowDef="let route; columns: displayedColumns$ | async;"></tr>
    </table>
  `,
  styles: [`
  `]
})
export class LongDistanceRoutesTableComponent implements OnInit {

  dataSource: MatTableDataSource<LdRoute>;
  displayedColumns$: Observable<Array<string>>;

  constructor(private pageWidthService: PageWidthService) {
    this.dataSource = new MatTableDataSource();
    this.displayedColumns$ = pageWidthService.current$.pipe(map(() => this.displayedColumns()));
  }

  ngOnInit(): void {

    const routes: LdRoute[] = [
      {group: 'GR5', name: 'Flanders', relationId: 3121667},
      {group: 'GR5', name: 'Wallonia', relationId: 3121668},
      {group: 'GR5', name: 'Variant Genk', relationId: 5951316},
      {group: 'GR5', name: 'Variant Hasselt', relationId: 290732},
      {group: 'GR5', name: 'Variant Testelt-Zichem (Demervariant) ', relationId: 2923308},
      {group: 'GR5', name: 'Rond afsluiting Kalmthoutse Heide (3 km)', relationId: 149843},
      {group: 'GR5A', name: 'Noord', relationId: 13658},
      {group: 'GR5A', name: 'Zuid', relationId: 2629186},
      {group: 'GR5A', name: 'Rond Canisvlietsche Kreek', relationId: 133437},
      {group: 'GR5A', name: 'South: Variant E2 / Verbinding GR5A-GR12', relationId: 13638},
      {group: 'GR5A', name: 'GR5A South: Variant Dode Ijzer', relationId: 6481535},
      {group: 'GR5A', name: 'GR5A South: Connection to GR123', relationId: 5556328},
      {group: 'GR5A', name: 'GR5A Noord: Variant Oostende Haven', relationId: 3194117},
    ];
    this.dataSource.data = routes;
  }

  private displayedColumns() {
    return ['group', 'name', 'relation'];
  }
}
