import {ChangeDetectionStrategy, Component} from '@angular/core';
import {Observable} from 'rxjs';
import {Issue} from '../store/issue/issue.state';
import {Store} from '@ngrx/store';
import {select} from '@ngrx/store';
import {AppState} from '../store/app.state';
import {selectIssueActive} from '../store/issue/issue.selectors';

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

  readonly issue$: Observable<Issue> = this.store.pipe(
    select(selectIssueActive)
  );

  constructor(private store: Store<AppState>) {
  }
}
