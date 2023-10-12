package io.github.sleepy_evelyn.territorial.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.sleepy_evelyn.territorial.block.entity.CorruptedBeaconBlockEntity;
import io.github.sleepy_evelyn.territorial.screen.CorruptedBeaconScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.BeaconUpdateC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.CommonTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class CorruptedBeaconScreen extends HandledScreen<CorruptedBeaconScreenHandler> {
    static final Identifier TEXTURE = new Identifier("textures/gui/container/beacon.png");
    protected static final Text PRIMARY_POWER_TEXT, SECONDARY_POWER_TEXT;

    protected final List<BeaconButtonWidget> buttons = Lists.newArrayList();
    @Nullable
    StatusEffect primaryEffect;
    @Nullable
    StatusEffect secondaryEffect;

    public CorruptedBeaconScreen(CorruptedBeaconScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 230;
        this.backgroundHeight = 219;

        handler.addListener(new ScreenHandlerListener() {
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {}

            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                var beaconScreenHandler = (CorruptedBeaconScreenHandler) handler;
                CorruptedBeaconScreen.this.primaryEffect = beaconScreenHandler.getPrimaryEffect();
                CorruptedBeaconScreen.this.secondaryEffect = beaconScreenHandler.getSecondaryEffect();
            }
        });
    }

    <T extends ClickableWidget & BeaconButtonWidget> void addButton(T button) {
        this.addDrawableChild(button);
        this.buttons.add(button);
    }

    protected void init() {
        super.init();
        this.buttons.clear();
        this.addButton(new DoneButtonWidget(this.x + 164, this.y + 107));
        this.addButton(new CancelButtonWidget(this.x + 190, this.y + 107));
        var effectsByLevel = getEffectsByLevel();

        int j, k, l;
        StatusEffect statusEffect;
        EffectButtonWidget effectButtonWidget;
        for(int i = 0; i <= 2; ++i) {
            j = effectsByLevel[i].length;
            k = j * 22 + (j - 1) * 2;

            for(l = 0; l < j; ++l) {
                statusEffect = effectsByLevel[i][l];
                effectButtonWidget = new EffectButtonWidget(this.x + 76 + l * 24 - k / 2, this.y + 22 + i * 25, statusEffect, true, i);
                effectButtonWidget.active = false;
                this.addButton(effectButtonWidget);
            }
        }
        j = effectsByLevel[3].length + 1;
        k = j * 22 + (j - 1) * 2;

        for(l = 0; l < j - 1; ++l) {
            statusEffect = effectsByLevel[3][l];
            effectButtonWidget = new EffectButtonWidget(this.x + 167 + l * 24 - k / 2, this.y + 47, statusEffect, false, 3);
            effectButtonWidget.active = false;
            this.addButton(effectButtonWidget);
        }

        EffectButtonWidget effectButtonWidget2
                = new LevelTwoEffectButtonWidget(this.x + 167 + (j - 1) * 24 - k / 2, this.y + 47, effectsByLevel[0][0]);
        effectButtonWidget2.visible = false;
        this.addButton(effectButtonWidget2);
    }

	public void handledScreenTick() {
        super.handledScreenTick();
        this.tickButtons();
    }

    void tickButtons() {
        int i = this.handler.getProperties();
        this.buttons.forEach((button) -> button.tick(i));
    }

	protected void drawForeground(GuiGraphics gui, int mouseX, int mouseY) {
		gui.drawCenteredShadowedText(this.textRenderer, PRIMARY_POWER_TEXT, 62, 10, 14737632);
        gui.drawCenteredShadowedText(this.textRenderer, SECONDARY_POWER_TEXT, 169, 10, 14737632);
    }

    protected void drawBackground(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;

		graphics.drawTexture(CorruptedBeaconScreen.TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
		var matrices = graphics.getMatrices();
		matrices.push();
		matrices.translate(0.0F, 0.0F, 100.0F);
		graphics.drawItem(new ItemStack(Items.NETHERITE_INGOT), i + 20, j + 109);
		graphics.drawItem(new ItemStack(Items.EMERALD), i + 41, j + 109);
		graphics.drawItem(new ItemStack(Items.DIAMOND), i + 41 + 22, j + 109);
		graphics.drawItem(new ItemStack(Items.GOLD_INGOT), i + 42 + 44, j + 109);
		graphics.drawItem(new ItemStack(Items.IRON_INGOT), i + 42 + 66, j + 109);
		matrices.pop();
    }

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		this.renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(graphics, mouseX, mouseY);
	}

    protected StatusEffect[][] getEffectsByLevel() {
        return CorruptedBeaconBlockEntity.EFFECTS_BY_LEVEL;
    }

    static {
        PRIMARY_POWER_TEXT = Text.translatable("block.minecraft.beacon.primary");
        SECONDARY_POWER_TEXT = Text.translatable("block.minecraft.beacon.secondary");
    }

    @Environment(EnvType.CLIENT)
    interface BeaconButtonWidget {
        void tick(int level);
    }

    @Environment(EnvType.CLIENT)
    class DoneButtonWidget extends IconButtonWidget {
        public DoneButtonWidget(int x, int y) {
            super(x, y, 90, 220, CommonTexts.DONE);
        }

        public void onPress() {
            CorruptedBeaconScreen.this.client.getNetworkHandler().sendPacket(new BeaconUpdateC2SPacket(Optional.ofNullable(CorruptedBeaconScreen.this.primaryEffect), Optional.ofNullable(CorruptedBeaconScreen.this.secondaryEffect)));
            CorruptedBeaconScreen.this.client.player.closeHandledScreen();
        }

        public void tick(int level) {
            this.active = (CorruptedBeaconScreen.this.handler).hasPayment() && CorruptedBeaconScreen.this.primaryEffect != null;
        }
	}

    @Environment(EnvType.CLIENT)
    class CancelButtonWidget extends IconButtonWidget {
        public CancelButtonWidget(int x, int y) {
            super(x, y, 112, 220, CommonTexts.CANCEL);
        }

        public void onPress() {
            client.player.closeHandledScreen();
        }

        public void tick(int level) {}
	}

    @Environment(EnvType.CLIENT)
    protected class EffectButtonWidget extends BaseButtonWidget {
        private final boolean primary;
        protected final int level;
        private StatusEffect effect;
        private Sprite sprite;
        private Text tooltip;

        public EffectButtonWidget(int x, int y, StatusEffect statusEffect, boolean primary, int level) {
            super(x, y);
            this.primary = primary;
            this.level = level;
            this.init(statusEffect);
        }

        protected void init(StatusEffect statusEffect) {
            this.effect = statusEffect;
            this.sprite = MinecraftClient.getInstance().getStatusEffectSpriteManager().getSprite(statusEffect);
            this.tooltip = this.getEffectName(statusEffect);
        }

        protected MutableText getEffectName(StatusEffect statusEffect) {
            return Text.translatable(statusEffect.getTranslationKey());
        }

        public void onPress() {
            if (!this.isDisabled()) {
                if (this.primary) {
                    CorruptedBeaconScreen.this.primaryEffect = this.effect;
                } else {
                    CorruptedBeaconScreen.this.secondaryEffect = this.effect;
                }

                CorruptedBeaconScreen.this.tickButtons();
            }
        }

		@Override
		protected void renderExtra(GuiGraphics graphics) {
			graphics.drawSprite(this.getX() + 2, this.getY() + 2, 0, 18, 18, this.sprite);
		}

        public void tick(int level) {
            this.active = this.level < level;
            this.setDisabled(this.effect == (this.primary ? CorruptedBeaconScreen.this.primaryEffect : CorruptedBeaconScreen.this.secondaryEffect));
        }

        protected MutableText getNarrationMessage() {
            return this.getEffectName(this.effect);
        }
	}

    @Environment(EnvType.CLIENT)
    protected class LevelTwoEffectButtonWidget extends EffectButtonWidget {
        public LevelTwoEffectButtonWidget(int x, int y, StatusEffect statusEffect) {
            super(x, y, statusEffect, false, 3);
        }

        protected MutableText getEffectName(StatusEffect statusEffect) {
            return (Text.translatable(statusEffect.getTranslationKey())).append(" II");
        }

        public void tick(int level) {
            if (CorruptedBeaconScreen.this.primaryEffect != null) {
                this.visible = true;
                this.init(CorruptedBeaconScreen.this.primaryEffect);
                super.tick(level);
            } else {
                this.visible = false;
            }

        }
    }

    @Environment(EnvType.CLIENT)
    abstract static class IconButtonWidget extends BaseButtonWidget {
        private final int u;
        private final int v;

        protected IconButtonWidget(int x, int y, int u, int v, Text message) {
            super(x, y, message);
            this.u = u;
            this.v = v;
        }

		@Override
		protected void renderExtra(GuiGraphics graphics) {
			graphics.drawTexture(CorruptedBeaconScreen.TEXTURE, this.getX() + 2, this.getY() + 2, this.u, this.v, 18, 18);
		}
    }

    @Environment(EnvType.CLIENT)
    abstract static class BaseButtonWidget extends PressableWidget implements BeaconButtonWidget {
        private boolean disabled;

        protected BaseButtonWidget(int x, int y) {
            this(x, y, Text.empty());
        }

        protected BaseButtonWidget(int x, int y, Text message) {
            super(x, y, 22, 22, message);
        }

        public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            RenderSystem.setShaderTexture(0, CorruptedBeaconScreen.TEXTURE);
            int j = 0;
            if (!this.active) {
                j += this.width * 2;
            } else if (this.disabled) {
                j += this.width;
            } else if (this.isHovered()) {
                j += this.width * 3;
            }
            graphics.drawTexture(CorruptedBeaconScreen.TEXTURE, this.getX(), this.getY(), j, 219, this.width, this.height);
			renderExtra(graphics);
        }

        protected abstract void renderExtra(GuiGraphics matrices);

        public boolean isDisabled() {
            return this.disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }
		@Override
		public void updateNarration(NarrationMessageBuilder builder) {
			this.appendDefaultNarrations(builder);
		}
    }
}
