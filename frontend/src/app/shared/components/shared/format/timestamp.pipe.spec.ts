import { Timestamp } from '@api/custom';
import { TimestampPipe } from './timestamp-pipe';

describe('timstamp pipe', () => {
  it('transform', () => {
    const timestamp: Timestamp = '2021-08-11T12:34:56Z';
    expect(new TimestampPipe('en').transform(timestamp)).toEqual('2021-08-11 12:34');
    expect(new TimestampPipe('nl').transform(timestamp)).toEqual('11-08-2021 12:34');
    expect(new TimestampPipe('de').transform(timestamp)).toEqual('11.08.2021 12:34');
    expect(new TimestampPipe('fr').transform(timestamp)).toEqual('11/08/2021 12:34');
  });
});
