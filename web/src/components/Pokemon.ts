import { GameObjects, Scene, Types } from "phaser";

interface IConfig {
    x: number;
    y: number;
    texture: string;
    nameTextX: number;
    nameTextY: number;
    scale: number;
    depth: number;
    name: string;
}

const nameTextStyle: Types.GameObjects.Text.TextStyle = {
    fontSize: "20px",
    color: "#010001",
};

export class Pokemon extends GameObjects.Image {
    private nameText: GameObjects.Text;

    constructor(
        scene: Scene,
        { x, y, nameTextX, nameTextY, scale, depth, texture, name }: IConfig
    ) {
        super(scene, x, y, texture);
        scene.add.existing(this);
        this.setScale(scale).setDepth(depth);

        this.nameText = scene.add
            .text(nameTextX, nameTextY, name.toUpperCase(), nameTextStyle)
            .setDepth(-1);
    }

    public setActive(value: boolean) {
        super.setActive(value);
        this.nameText.setActive(value);
        return this;
    }

    public deactivate() {
        this.setActive(false);
        this.setVisible(false);
    }

    public setVisible(value: boolean) {
        super.setVisible(value);
        this.nameText.setVisible(value);
        return this;
    }
}
