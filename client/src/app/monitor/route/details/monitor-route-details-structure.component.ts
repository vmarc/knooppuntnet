import { OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MonitorRouteRelation } from '@api/common/monitor/monitor-route-relation';
import { MonitorRouteRelationRow } from './monitor-route-relation-row';

@Component({
  selector: 'kpn-monitor-route-details-structure',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table mat-table [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.route.relation-table.nr"
        >
          Nr
        </th>
        <td mat-cell *matCellDef="let row; let i = index">
          {{ i + 1 }}
        </td>
      </ng-container>

      <ng-container matColumnDef="relation">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.route.relation-table.relation"
        >
          Relation
        </th>
        <td mat-cell *matCellDef="let row">
          <kpn-osm-link-relation
            [relationId]="row.relation.relationId"
            [title]="row.relation.relationId.toString()"
          >
          </kpn-osm-link-relation>
        </td>
      </ng-container>

      <ng-container matColumnDef="name">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.route.relation-table.name"
        >
          Name
        </th>
        <td mat-cell *matCellDef="let row">
          <div *ngIf="row.level === 1" class="level-1">{{ row.name }}</div>
          <div *ngIf="row.level === 2" class="level-2">{{ row.name }}</div>
          <div *ngIf="row.level === 3" class="level-3">{{ row.name }}</div>
          <div *ngIf="row.level === 4" class="level-4">{{ row.name }}</div>
          <div *ngIf="row.level === 5" class="level-5">{{ row.name }}</div>
        </td>
      </ng-container>

      <ng-container matColumnDef="role">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.route.relation-table.role"
        >
          Role
        </th>
        <td mat-cell *matCellDef="let row">
          {{ row.relation.role }}
        </td>
      </ng-container>

      <ng-container matColumnDef="distance">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.route.relation-table.distance"
        >
          Distance
        </th>
        <td mat-cell *matCellDef="let row">
          <div class="distance">{{ row.relation.osmDistance | distance }}</div>
        </td>
      </ng-container>

      <ng-container matColumnDef="survey">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.route.relation-table.survey"
        >
          Survey
        </th>
        <td mat-cell *matCellDef="let row">
          {{ row.relation.survey | day }}
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
    </table>
  `,
  styles: [
    `
      .distance {
        white-space: nowrap;
        text-align: right;
        width: 100%;
      }

      .level-1 {
      }

      .level-2 {
        margin-left: 1.5em;
      }

      .level-3 {
        margin-left: 3em;
      }

      .level-4 {
        margin-left: 4.5em;
      }

      .level-5 {
        margin-left: 6em;
      }
    `,
  ],
})
export class MonitorRouteDetailsStructureComponent implements OnInit {
  @Input() relation: MonitorRouteRelation;

  readonly dataSource = new MatTableDataSource<MonitorRouteRelationRow>();
  readonly displayedColumns = [
    'nr',
    'relation',
    'name',
    'role',
    'distance',
    'survey',
  ];

  ngOnInit(): void {
    this.dataSource.data = this.flattenRelationTree();
  }

  private flattenRelationTree(): MonitorRouteRelationRow[] {
    const rowsLevel2 = this.relation.relations.flatMap((relationLevel2) => {
      const rowsLevel3 = relationLevel2.relations.flatMap((relationLevel3) => {
        const rowsLevel4 = relationLevel3.relations.flatMap(
          (relationLevel4) => {
            const rowsLevel5 = relationLevel4.relations.map(
              (relationLevel5) => {
                return this.row(5, relationLevel5);
              }
            );
            return [this.row(4, relationLevel4), ...rowsLevel5];
          }
        );
        return [this.row(3, relationLevel3), ...rowsLevel4];
      });
      return [this.row(2, relationLevel2), ...rowsLevel3];
    });
    return [this.row(1, this.relation), ...rowsLevel2];
  }

  private row(
    level: number,
    relation: MonitorRouteRelation
  ): MonitorRouteRelationRow {
    return {
      level,
      name: this.relationName(relation),
      relation,
    };
  }

  private relationName(relation: MonitorRouteRelation): string {
    let name = '?';
    if (relation.name) {
      name = relation.name;
    } else if (relation.from && relation.to) {
      name = `${relation.from} - ${relation.to}`;
    }
    return name;
  }
}
