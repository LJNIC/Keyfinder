package com.ljnic;

import org.codetome.zircon.api.Position;
import org.codetome.zircon.api.Size;
import org.codetome.zircon.api.SwingTerminalBuilder;
import org.codetome.zircon.api.animation.Animation;
import org.codetome.zircon.api.animation.AnimationHandler;
import org.codetome.zircon.api.animation.AnimationResource;
import org.codetome.zircon.api.builder.AnimationBuilder;
import org.codetome.zircon.api.builder.ScreenBuilder;
import org.codetome.zircon.api.builder.TerminalBuilder;
import org.codetome.zircon.api.color.ANSITextColor;
import org.codetome.zircon.api.color.TextColorFactory;
import org.codetome.zircon.api.component.Button;
import org.codetome.zircon.api.component.ColorTheme;
import org.codetome.zircon.api.component.Panel;
import org.codetome.zircon.api.component.builder.ButtonBuilder;
import org.codetome.zircon.api.component.builder.ColorThemeBuilder;
import org.codetome.zircon.api.component.builder.LabelBuilder;
import org.codetome.zircon.api.component.builder.PanelBuilder;
import org.codetome.zircon.api.font.Font;
import org.codetome.zircon.api.graphics.Layer;
import org.codetome.zircon.api.resource.CP437TilesetResource;
import org.codetome.zircon.api.resource.REXPaintResource;
import org.codetome.zircon.api.screen.Screen;
import org.codetome.zircon.api.terminal.Terminal;
import org.codetome.zircon.internal.component.DefaultColorTheme;

import java.io.InputStream;

public class Test {
    private static final Size TERMINAL_SIZE = Size.of(50, 30);
    private static final Position LEFT_POS = Position.of(8, 5);
    private static final Position RIGHT_POS = Position.of(29, 5);

    public static void main(String[] args) {
        final Terminal terminal = SwingTerminalBuilder.newBuilder()
                .font(CP437TilesetResource.WANDERLUST_16X16.toFont())
                .initialTerminalSize(TERMINAL_SIZE)
                .build();
        final Screen screen = ScreenBuilder.createScreenFor(terminal);
        screen.setCursorVisibility(false);

        final Panel panel = PanelBuilder.newBuilder()
                .wrapWithBox()
                .title("Animation example")
                .size(TERMINAL_SIZE)
                .build();

        panel.addComponent(LabelBuilder.newBuilder()
                .text("Looped:")
                .position(LEFT_POS.withRelativeRow(-3).withRelativeColumn(-1))
                .build());
        panel.addComponent(LabelBuilder.newBuilder()
                .text("Non-looped:")
                .position(RIGHT_POS.withRelativeRow(-3).withRelativeColumn(-1))
                .build());
        screen.addComponent(panel);

        screen.display();


        AnimationBuilder first = AnimationResource.loadAnimationFromStream(Test.class.getResourceAsStream("/skull.zap"));
        AnimationBuilder second = first.createCopy();
        first.loopCount(0);
        second.loopCount(1);
        for (int i = 0; i < first.getLength(); i++) {
            first.addPosition(LEFT_POS);
            second.addPosition(RIGHT_POS);
        }
        Animation leftAnim = first.build();
        Animation rightAnim = second.build();

        final AnimationHandler animationHandler = new AnimationHandler(screen);
        animationHandler.addAnimation(leftAnim);
        animationHandler.addAnimation(rightAnim);

    }

}
