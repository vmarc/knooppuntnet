PROXY_CONFIG = {
  "/oauth2/**": {
    target: "http://localhost:9010",
    secure: false,
  },
  "/login/**": {
    target: "http://localhost:9010",
    secure: false,
  },
  "/user": {
    target: "http://localhost:9010",
    secure: false,
  },
  "/api/**": {
    target: "http://localhost:9010",
    secure: false,
  },
};

module.exports = PROXY_CONFIG;
