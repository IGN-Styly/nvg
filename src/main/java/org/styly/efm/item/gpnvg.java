package org.styly.efm.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.styly.efm.rederer.GPNVGRenderer;
import org.styly.efm.rederer.GPNVG_Renderer_ST;
import org.styly.efm.registries.DataCompReg;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;
import java.util.function.Consumer;

public class gpnvg extends ArmorItem implements GeoItem {
    private  boolean variant;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private Boolean toggle = false;

    public gpnvg(Holder<ArmorMaterial> pMaterial, Type pType, Properties pProperties, boolean variant) {
        super(pMaterial, pType, pProperties);
        this.variant=variant;
    }


    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;
            private NVGItemRenderer renderer2;
            @Override
            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (this.renderer == null)
                    if(variant) this.renderer= new GPNVG_Renderer_ST();
                    else this.renderer = new GPNVGRenderer();

                return this.renderer;
            }
            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer2 == null)
                    if(variant) this.renderer2= new NVGItemRenderer();
                    else this.renderer2 = new NVGItemRenderer();


                return this.renderer2;
            }
        });

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 2, state -> {
            // Apply our generic idle animation.
            // Whether it plays or not is decided down below.


            // Let's gather some data from the state to use below
            // This is the entity that is currently wearing/holding the item
            if (!(state.getData(DataTickets.ENTITY) instanceof Player entity)) {
                return PlayState.CONTINUE;
            }

            toggle = Objects.requireNonNull(entity.getItemBySlot(EquipmentSlot.HEAD).get(DataCompReg.NVG_TOGGLE)).toggle();

            if (toggle) {
                state.getController().setAnimation(RawAnimation.begin().thenPlayAndHold("to_on"));
            } else {
                state.getController().setAnimation(RawAnimation.begin().thenPlayAndHold("to_off"));
            }

            if (toggle) {
                state.getController().setAnimation(RawAnimation.begin().thenPlayAndHold("to_on"));
            } else {
                state.getController().setAnimation(RawAnimation.begin().thenPlayAndHold("to_off"));
            }

            // We'll just have ArmorStands always animate, so we can return here
            return PlayState.CONTINUE;

        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
