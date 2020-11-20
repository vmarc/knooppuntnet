import {Component, OnInit} from '@angular/core';

export interface Data {
  id: number;
  name: string;
  expectedRoutes: number;
  routes: string;
  disabled: boolean;
}

@Component({
  selector: 'app-routes',
  template: `
    <app-header></app-header>
    <div class="send-request">
      <button nz-button nzType="primary" [disabled]="setOfCheckedId.size === 0" [nzLoading]="loading"
              (click)="sendRequest()">
        Send Request
      </button>
      <span>Selected {{ setOfCheckedId.size }} items</span>
    </div>
    <nz-table
      #rowSelectionTable
      nzShowPagination="true"
      nzPaginationPosition="both"
      nzShowSizeChanger="true"
      nzBordered="true"
      nzSize="small"
      [nzData]="listOfData"
      (nzCurrentPageDataChange)="onCurrentPageDataChange($event)">
      <thead>
      <tr>
        <th rowSpan="2" [nzChecked]="checked" [nzIndeterminate]="indeterminate"
            (nzCheckedChange)="onAllChecked($event)"></th>
        <th rowSpan="2">Name</th>
        <th colSpan="2">Routes</th>
      </tr>
      <tr>
        <th>Expected</th>
        <th>Found</th>
      </tr>
      </thead>
      <tbody>
      <tr *ngFor="let data of rowSelectionTable.data">
        <td
          [nzChecked]="setOfCheckedId.has(data.id)"
          [nzDisabled]="data.disabled"
          (nzCheckedChange)="onItemChecked(data.id, $event)"
        ></td>
        <td>{{ data.name }}</td>
        <td>{{ data.expectedRoutes }}</td>
        <td>{{ data.routes }}</td>
      </tr>
      </tbody>
    </nz-table>
  `,
  styles: [`
    .send-request {
      margin-bottom: 16px;
    }

    .send-request span {
      margin-left: 8px;
    }
  `]
})
export class RoutesComponent implements OnInit {

  checked = false;
  loading = false;
  indeterminate = false;
  listOfData: Data[] = [];
  listOfCurrentPageData: Data[] = [];
  setOfCheckedId = new Set<number>();

  updateCheckedSet(id: number, checked: boolean): void {
    if (checked) {
      this.setOfCheckedId.add(id);
    } else {
      this.setOfCheckedId.delete(id);
    }
  }

  onCurrentPageDataChange(listOfCurrentPageData: Data[]): void {
    this.listOfCurrentPageData = listOfCurrentPageData;
    this.refreshCheckedStatus();
  }

  refreshCheckedStatus(): void {
    const listOfEnabledData = this.listOfCurrentPageData.filter(({disabled}) => !disabled);
    this.checked = listOfEnabledData.every(({id}) => this.setOfCheckedId.has(id));
    this.indeterminate = listOfEnabledData.some(({id}) => this.setOfCheckedId.has(id)) && !this.checked;
  }

  onItemChecked(id: number, checked: boolean): void {
    this.updateCheckedSet(id, checked);
    this.refreshCheckedStatus();
  }

  onAllChecked(checked: boolean): void {
    this.listOfCurrentPageData.filter(({disabled}) => !disabled).forEach(({id}) => this.updateCheckedSet(id, checked));
    this.refreshCheckedStatus();
  }

  sendRequest(): void {
    this.loading = true;
    const requestData = this.listOfData.filter(data => this.setOfCheckedId.has(data.id));
    console.log(requestData);
    setTimeout(() => {
      this.setOfCheckedId.clear();
      this.refreshCheckedStatus();
      this.loading = false;
    }, 1000);
  }

  ngOnInit(): void {
    this.listOfData = new Array(100).fill(0).map((_, index) => {
      return {
        id: index,
        name: `Node ${index}`,
        expectedRoutes: 3,
        routes: `${index}-${index + 1}, ${index}-${index + 5}`,
        disabled: index % 2 === 0
      };
    });
  }
}
