import {DataSource} from '@angular/cdk/collections';
import {MatPaginator} from '@angular/material';
import {merge, Observable, of as observableOf} from 'rxjs';
import {map} from 'rxjs/operators';
import {NodeInfo} from "../../../../kpn/shared/node-info";

export class SubsetOrphanNodesTableItem {
  constructor(public row: number,
              public node: NodeInfo) {
  }
}

export class SubsetOrphanNodesTableDataSource extends DataSource<SubsetOrphanNodesTableItem> {

  data: SubsetOrphanNodesTableItem[];

  constructor(
    private paginator: MatPaginator,
    nodes: Array<NodeInfo>) {
    super();
    let row = 1;
    this.data = nodes.map(n => new SubsetOrphanNodesTableItem(row++, n))
  }

  connect(): Observable<SubsetOrphanNodesTableItem[]> {
    const dataMutations = [
      observableOf(this.data),
      this.paginator.page
    ];
    this.paginator.length = this.data.length;
    return merge(...dataMutations).pipe(map(() => {
      return this.getPagedData([...this.data]);
    }));
  }

  disconnect() {
  }

  private getPagedData(data: SubsetOrphanNodesTableItem[]) {
    const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
    return data.splice(startIndex, this.paginator.pageSize);
  }

}
