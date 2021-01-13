import {AppState} from '../../store/app.state';

export const initialState: Page6State = {
  field1: 'one',
  field2: 'two',
  field3: 'three',
  field4: 'four'
};

export interface Page6State {
  field1: string;
  field2: string;
  field3: string;
  field4: string;
}

export const page6FeatureKey = 'page6';

export interface Page6RootState extends AppState {
  [page6FeatureKey]: Page6State;
}
