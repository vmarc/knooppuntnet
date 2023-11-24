import { OAuthModuleConfig } from 'angular-oauth2-oidc';

export const authModuleConfig: OAuthModuleConfig = {
  resourceServer: {
    allowedUrls: ['https://www.openstreetmap.org/oauth2/userinfo'],
    sendAccessToken: true,
  }
};
