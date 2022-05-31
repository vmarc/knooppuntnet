import { Timestamp } from '@api/custom/timestamp';
import { TimestampUtil } from './timestamp-util';

describe('timestamp-util', () => {
  it('timestamp parts', () => {
    const timestamp: Timestamp = '2021-08-11T12:34:56';

    expect(TimestampUtil.formatted(timestamp)).toEqual('2021-08-11 12:34');
    expect(TimestampUtil.year(timestamp)).toEqual('2021');
    expect(TimestampUtil.month(timestamp)).toEqual('08');
    expect(TimestampUtil.dayPart(timestamp)).toEqual('11');
    expect(TimestampUtil.hour(timestamp)).toEqual('12');
    expect(TimestampUtil.minute(timestamp)).toEqual('34');
    expect(TimestampUtil.second(timestamp)).toEqual('56');
  });
});
