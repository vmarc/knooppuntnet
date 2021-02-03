import {ChangeDetectionStrategy, Component, EventEmitter, Output} from '@angular/core';
import {Issue} from '../store/issue/issue.state';
import {Store} from '@ngrx/store';
import {select} from '@ngrx/store';
import {AppState} from '../store/app.state';
import {Observable} from 'rxjs';
import {selectIssues} from '../store/issue/issue.selectors';

@Component({
  selector: 'app-issue-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <input
      placeholder="Search ..."
      #searchInput
      type="text"
      (input)="onSearch(searchInput.value)"
    />
    <ul>
      <li *ngFor="let issue of issues$ | async; trackBy: trackByIssues">
        <h3>
          <a [routerLink]="issue.id">{{ issue.title }}</a>
          <small>{{ issue.priority }}</small>
        </h3>
        <p>{{ issue.description }}</p>
        <button (click)="onResolve(issue)" [disabled]="issue.resolved">
          {{ issue.resolved ? "Resolved" : "Resolve" }}
        </button>
      </li>
    </ul>
  `,
  styles: [`
  `]
})
export class IssueListComponent {

  @Output() search = new EventEmitter<string>();
  @Output() resolve = new EventEmitter<Issue>();

  readonly issues$: Observable<Issue[]> = this.store.select(selectIssues);

  constructor(private store: Store<AppState>) {
  }

  onSearch(text: string): void {
    this.search.emit(text);
  }

  onResolve(issue: Issue): void {
    this.resolve.emit(issue);
  }

  trackByIssues(index: number, issue: Issue): string {
    return issue.id;
  }
}
