import {createAction} from '@ngrx/store';
import {props} from '@ngrx/store';
import {Issue} from './issue.state';

export const submit = createAction(
  '[Issue] submit',
  props<{ issue: Issue }>()
);
