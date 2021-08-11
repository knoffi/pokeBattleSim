// TODO make dev easy again
export const isProd = true; // window.location.hostname !== "localhost";
export const DEV = isProd
    ? {}
    : {
          inBattleImmediately: false,
      };
