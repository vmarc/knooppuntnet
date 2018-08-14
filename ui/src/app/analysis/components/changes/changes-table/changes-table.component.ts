import {AfterViewInit, Component, Input, ViewChild} from '@angular/core';
import {MatPaginator, MatTableDataSource} from '@angular/material';
import {ChangesParameters} from "../../../../kpn/shared/changes/filter/changes-parameters";
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {ChangesPage} from "../../../../kpn/shared/changes-page";
import {ChangeSetSummaryInfo} from "../../../../kpn/shared/change-set-summary-info";

@Component({
  selector: 'kpn-changes-table',
  templateUrl: './changes-table.component.html',
  styleUrls: ['./changes-table.component.scss']
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
    this.parameters.itemsPerPage = this.paginator.pageSize;
    this.parameters.pageIndex = this.paginator.pageIndex;
    this.appService.changes(this.parameters).subscribe(response => {
      this.response = response;
      this.dataSource.data = response.result.changes;
      this.paginator.length = response.result.totalCount;
    });
  }

  rowNumber(index: number): number {
    return this.parameters.pageIndex * this.parameters.itemsPerPage + index + 1;
  }

}
