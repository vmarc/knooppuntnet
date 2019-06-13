PROXY_CONFIG = {
  "/api/**": {
    "target": "http://localhost:9003",
    "secure": false
  },
  "/tiles/**": {
    "target": "https://experimental.knooppuntnet.nl",
    "secure": false
  },
  "**": {
    "target": "http://localhost:9003",
    "secure": false,
    "bypass": function (req) {
      if (req.headers.accept.indexOf("html") !== -1) {
        return "/index.html";
      }
    }
  }
};

module.exports = PROXY_CONFIG;
