// TODO make dev easy again
export const isProd = process.env.NODE_ENV === "production";
export const DEV = isProd
    ? {}
    : {
          inBattleImmediately: true,
          enableSceneWatcher: false,
      };
