PROXY_CONFIG = {
  "/api/**": {
    target: "https://knooppuntnet.nl",
    secure: false,
  },
  "/admin-api/**": {
    target: "https://knooppuntnet.nl",
    secure: false,
  },
  "/websocket/*": {
    target: "ws://knooppuntnet.nl",
    secure: false,
    ws: true,
  },
  "/tiles/**": {
    target: "https://knooppuntnet.nl",
    changeOrigin: true,
    secure: false,
  },
  "/tiles-history/**": {
    target: "https://knooppuntnet.nl",
    changeOrigin: true,
    secure: false,
  },
  "/images/**": {
    target: "https://knooppuntnet.nl",
    changeOrigin: true,
    secure: false,
  },
  "/videos/**": {
    target: "https://knooppuntnet.nl",
    changeOrigin: true,
    secure: false,
  },
};

module.exports = PROXY_CONFIG;
