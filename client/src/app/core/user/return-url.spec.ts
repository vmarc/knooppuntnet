import { ReturnUrl } from './return-url';

describe('returnUrl', () => {
  it('extract returnUrl', () => {
    const url =
      '?page=/analysis/network/1066154/changes~pageSize=25,pageIndex=0,impact=true&oauth_token=ufuxk4nq8onjjN9jpKPOndzBWhkoZabIWD61JBJ5&oauth_verifier=jyyc1dlKfcj3M4MBqwZd';
    expect(ReturnUrl.fromUrl(url)).toEqual(
      '/analysis/network/1066154/changes?pageSize=25&pageIndex=0&impact=true'
    );
  });

  it('encode url', () => {
    const url =
      '/analysis/network/1066154/changes?pageSize=25&pageIndex=0&impact=true';
    expect(ReturnUrl.encode(url)).toEqual(
      '/analysis/network/1066154/changes~pageSize=25,pageIndex=0,impact=true'
    );
  });

  it('encode url, "/" if url contains "~" character', () => {
    const url = 'bla~bla';
    expect(ReturnUrl.encode(url)).toEqual('/');
  });

  it('encode url, "/" if url contains "," character', () => {
    const url = 'bla,bla';
    expect(ReturnUrl.encode(url)).toEqual('/');
  });
});
