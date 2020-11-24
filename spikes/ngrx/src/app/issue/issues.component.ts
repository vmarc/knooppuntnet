import {Component} from '@angular/core';
import {Issue} from '../store/issue/issue.state';

@Component({
  selector: 'app-issues',
  template: `
    <app-new-issue (submitNew)="onSubmit($event)"></app-new-issue>
    <app-issue-list
      (resolve)="onResolve($event)"
      (search)="onSearch($event)"
    ></app-issue-list>
    <ng-template #loading>Loading...</ng-template>
  `,
  styles: [`
  `]
})
export class IssuesComponent {

  onSearch(text: string): void {
  }

  onResolve(issue: Issue): void {
  }

  onSubmit(issue: Issue): void {
  }
}
