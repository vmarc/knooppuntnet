import {AfterViewInit, Component, Input, ViewChild} from '@angular/core';
import {MatPaginator, MatTableDataSource} from '@angular/material';
import {ChangesParameters} from "../../../kpn/shared/changes/filter/changes-parameters";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {ChangesPage} from "../../../kpn/shared/changes-page";
import {ChangeSetSummaryInfo} from "../../../kpn/shared/change-set-summary-info";

@Component({
  selector: 'kpn-changes-table',
  template: `
    <mat-paginator
      [pageIndex]="0"
      [pageSize]="parameters.itemsPerPage"
      [pageSizeOptions]="[25, 50, 100, 250, 1000]">
    </mat-paginator>

    <div *ngIf="response">

      <mat-divider></mat-divider>
      <table mat-table [dataSource]="dataSource" class="kpn-columns-table">

        <ng-container matColumnDef="rowNumber">
          <td mat-cell *matCellDef="let changeset; let i = index">
            {{rowNumber(i)}}
          </td>
        </ng-container>

        <ng-container matColumnDef="changeset">
          <td mat-cell *matCellDef="let changeset">
            {{changeset.summary.key.changeSetId + " / " + changeset.summary.key.replicationNumber }}
          </td>
        </ng-container>

        <tr mat-row *matRowDef="let changeset; columns: displayedColumns;"></tr>

      </table>
      <json [object]="response"></json>
    </div>
  `
})
export class ChangesTableComponent implements AfterViewInit {

  response: ApiResponse<ChangesPage>;

  @Input() parameters: ChangesParameters;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  dataSource: MatTableDataSource<ChangeSetSummaryInfo>;

  displayedColumns = ['rowNumber', 'changeset'];

  constructor(private appService: AppService) {
  }

  ngAfterViewInit() {
    this.dataSource = new MatTableDataSource([]);
    this.paginator.page.subscribe(event => this.reload());
    this.reload();
  }

  private reload() {
    this.updateParameters();
    this.appService.changes(this.parameters).subscribe(response => {
      this.response = response;
      this.dataSource.data = response.result.changes.toArray();
      this.paginator.length = response.result.totalCount;
    });
  }

  rowNumber(index: number): number {
    return this.parameters.pageIndex * this.parameters.itemsPerPage + index + 1;
  }

  private updateParameters() {
    this.parameters = new ChangesParameters(
      this.parameters.subset,
      this.parameters.networkId,
      this.parameters.routeId,
      this.parameters.nodeId,
      this.parameters.year,
      this.parameters.month,
      this.parameters.day,
      this.paginator.pageSize,
      this.paginator.pageIndex,
      this.parameters.impact
    );
  }
}
