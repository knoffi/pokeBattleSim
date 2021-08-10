import { GameObjects } from "phaser";
import { Color } from "./Color";

type Style = Partial<GameObjects.TextStyle>;

export const TextConfig: { [key: string]: Style } = {
    title: {
        fontFamily: "Arial",
        fontSize: "10rem",
        color: Color.WhiteSilver,
    },
    gameOverTitle: {
        fontFamily: "Arial",
        fontSize: "10rem",
        color: Color.Orange,
    },
    buttonSm: {
        fontFamily: "Arial",
        fontSize: "7rem",
        color: Color.WhiteSilver,
    },
    version: {
        fontFamily: "Arial",
        color: Color.WhiteSilver,
        fontSize: "2rem",
    },
    xl: {
        fontFamily: "Arial",
        fontSize: "118px",
        color: Color.WhiteSilver,
    },
    lg: {
        fontFamily: "Arial",
        fontSize: "20px",
        color: Color.WhiteSilver,
    },
    md: {
        fontFamily: "Arial",
        fontSize: "16px",
        color: Color.WhiteSilver,
    },
    sm: {
        fontFamily: "Arial",
        fontSize: "12px",
        color: Color.WhiteSilver,
    },
    debug: {
        fontFamily: "Courier",
        fontSize: "12px",
        color: Color.HackerGreen,
    },
};
