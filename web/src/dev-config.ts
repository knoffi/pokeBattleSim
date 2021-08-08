export const isProd = window.location.hostname !== "localhost";
export const DEV = isProd
    ? {}
    : {
          inBattleImmediately: true,
      };
