import { GameObjects } from "phaser";
import { Color } from "./Color";

type Style = Partial<GameObjects.TextStyle>;

export const TextConfig: { [key in "md" | "debug"]: Style } = {
    md: {
        fontFamily: "PressStart2P",
        fontSize: "12px",
        color: "#010001",
    },
    debug: {
        fontFamily: "Courier",
        fontSize: "12px",
        color: Color.HackerGreen,
    },
};
