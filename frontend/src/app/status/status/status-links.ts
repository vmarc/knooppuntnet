import { ActionTimestamp } from '@api/common/status';

export class StatusLinks {
  year: string;
  month: string;
  week: string;
  day: string;
  hour: string;

  constructor(timestamp: ActionTimestamp, root: string) {
    this.year = `${root}/year/${timestamp.weekYear}`;
    this.month = `${root}/month/${timestamp.year}/${timestamp.month}`;
    this.week = `${root}/week/${timestamp.weekYear}/${timestamp.weekWeek}`;
    this.day = `${root}/day/${timestamp.year}/${timestamp.month}/${timestamp.day}`;
    this.hour = `${root}/hour/${timestamp.year}/${timestamp.month}/${timestamp.day}/${timestamp.hour}`;
  }
}
