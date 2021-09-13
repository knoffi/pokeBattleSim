/* eslint-disable max-classes-per-file */
/* eslint-disable @typescript-eslint/no-namespace */

// Typings for Phaser 3 rexrainbow shader color inversion plugin
// Original docs: https://rexrainbow.github.io/phaser3-rex-notes/docs/site/shader-inverse/

type RexTarget = Phaser.GameObjects.GameObject | Phaser.Cameras.Scene2D.Camera;

declare namespace Phaser.Plugins {
    interface PluginManager {
        get<T extends (string & {}) | "rexInversePipeline">(
            name: T
        ): T extends "rexInversePipeline"
            ? RexPlugin
            : Phaser.Plugins.BasePlugin | Function;
    }

    interface RexPlugin {
        /** Remove effect from the target */
        remove(target: RexTarget): void;
        /** Add effect from the target */
        add(target: RexTarget, config: {}): void;
        get(target: RexTarget): RexPipeline[];
    }

    interface RexPipeline {
        /** 0(original color) ~ 1(inverse color) */
        intensity: number;
        /** @param radius 0(original color) ~ 1(inverse color) */
        setIntensity(radius: number): void;
    }
}
