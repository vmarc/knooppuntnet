PROXY_CONFIG = {
  "/api/**": {
    target: "http://localhost:9010",
    secure: false,
  },
};

module.exports = PROXY_CONFIG;
