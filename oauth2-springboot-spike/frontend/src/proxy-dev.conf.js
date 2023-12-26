PROXY_CONFIG = {
  "/api/**": {
    target: "http://localhost:9010",
    secure: false,
  },
  "/oauth2/**": {
    target: "http://localhost:9010",
    secure: false,
  },
  "/login/**": {
    target: "http://localhost:9010",
    secure: false,
  },
};

module.exports = PROXY_CONFIG;
