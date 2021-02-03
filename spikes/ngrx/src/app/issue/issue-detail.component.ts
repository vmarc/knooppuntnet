import {ChangeDetectionStrategy, Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {select} from '@ngrx/store';
import {Observable} from 'rxjs';
import {AppState} from '../store/app.state';
import {selectIssueActive} from '../store/issue/issue.selectors';
import {Issue} from '../store/issue/issue.state';

@Component({
  selector: 'app-issue-detail',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <section *ngIf="issue$ | async as issue">
      <h2>
        {{ issue.title }}
        <small>{{ issue.priority }}</small>
      </h2>
      <p>{{ issue.description }}</p>
    </section>
  `,
  styles: [`
  `]
})
export class IssueDetailComponent {

  readonly issue$: Observable<Issue> = this.store.select(selectIssueActive);

  constructor(private store: Store<AppState>) {
  }
}
