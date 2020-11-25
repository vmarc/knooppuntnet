export type Priority = 'low' | 'medium' | 'high';

export interface Issue {
  id: string;
  title: string;
  description: string;
  priority: Priority;
  resolved: boolean;
}

export interface Issues {
  [id: string]: Issue;
}

export interface Filter {
  text: string;
}

export interface IssueState {
  issues: Issues;
  selected: string[];
  filter: Filter;
}

const issues: Issues = {
  1: {
    id: '1',
    title: 'title1',
    description: 'description1',
    priority: 'low',
    resolved: false
  },
  2: {
    id: '2',
    title: 'title2',
    description: 'description2',
    priority: 'medium',
    resolved: true
  }
};

export const initialState: IssueState = {
  issues,
  selected: [],
  filter: {text: ''}
};
