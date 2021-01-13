import {createFeatureSelector} from '@ngrx/store';
import {page6FeatureKey} from './page6.state';
import {Page6State} from './page6.state';
import {Page6RootState} from './page6.state';

export const selectPage6State = createFeatureSelector<Page6RootState, Page6State>(page6FeatureKey);
