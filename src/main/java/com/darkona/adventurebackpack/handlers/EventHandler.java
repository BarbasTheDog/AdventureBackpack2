package com.darkona.adventurebackpack.handlers;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.common.Actions;
import com.darkona.adventurebackpack.events.HoseSpillEvent;
import com.darkona.adventurebackpack.events.HoseSuckEvent;
import com.darkona.adventurebackpack.inventory.SlotTool;
import com.darkona.adventurebackpack.item.ItemAdventureBackpack;
import com.darkona.adventurebackpack.item.ItemHose;
import com.darkona.adventurebackpack.network.CycleToolMessage;
import com.darkona.adventurebackpack.util.LogHelper;
import com.darkona.adventurebackpack.util.Wearing;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created on 11/10/2014
 * @author Darkona
 * @see com.darkona.adventurebackpack.common.Actions
 */
public class EventHandler {

    long systemTime = 0;
    @SubscribeEvent
    public void onTickPassed(LivingEvent.LivingJumpEvent event) {
        if (event.entity != null &&
                event.entityLiving instanceof EntityPlayer &&
                ((EntityPlayer) event.entityLiving).onGround &&
                Wearing.isWearingBoots(((EntityPlayer) event.entityLiving))
                ) {
            Actions.pistonBootsJump(((EntityPlayer) event.entityLiving));
        }
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event) {
        if (event.entity != null &&
                event.entityLiving instanceof EntityPlayer &&
                Wearing.isWearingBoots(((EntityPlayer) event.entityLiving)) &&
                event.distance < 8) {
            event.setCanceled(true);
        }
    }

    /*

    This is the old way to do it, it messes with the animation and I don't like it, but it works.
    I'll leave it here for documentation purposes.

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (event.phase == TickEvent.Phase.START) {
            dWheel = Mouse.getDWheel() / 120;
            if (player != null) {
                if (player.isSneaking()) {
                    ItemStack backpack = player.getCurrentArmor(2);
                    if (backpack != null && backpack.getItem() instanceof ItemAdventureBackpack) {

                        Minecraft.getMinecraft().playerController.updateController();
                        if (player.getCurrentEquippedItem() != null) {
                            if (SlotTool.isValidTool(player.getCurrentEquippedItem())) {
                                isTool = true;
                                theSlot = player.inventory.currentItem;
                            }
                            if (player.getCurrentEquippedItem().getItem() instanceof ItemHose) {
                                isHose = true;
                                theSlot = player.inventory.currentItem;
                            }
                        }
                    }
                } else {
                    theSlot = -1;
                }
            }
        }

        if (event.phase == TickEvent.Phase.END) {
            if (player != null) {
                if (theSlot > -1 && dWheel != Mouse.getDWheel()) {

                    if (isHose) {
                        player.inventory.currentItem = theSlot;
                        LogHelper.info("Sending hose switch message");
                        AdventureBackpack.networkWrapper.sendToServer(new CycleToolMessage(-1, theSlot, false));
                    }

                    if (isTool) {

                        LogHelper.info("Sending tool cycle message");
                        player.inventory.currentItem = theSlot;
                        AdventureBackpack.networkWrapper.sendToServer(new CycleToolMessage(dWheel - Mouse.getDWheel(), theSlot, true));
                    }

                }


            }
            theSlot = -1;
            isHose = false;
            isTool = false;
        }
    }
*/

    @SubscribeEvent
    public void mouseWheelDetect(MouseEvent event) {
        /*Special thanks go to MachineMuse, both for inspiration and the event. God bless you girl.*/
        Minecraft mc = Minecraft.getMinecraft();
        int dWheel = event.dwheel;
        if (dWheel != 0) {
            LogHelper.debug("Mouse Wheel moving");
            EntityClientPlayerMP player = mc.thePlayer;
            systemTime = mc.getSystemTime() - systemTime;

            if (player != null && !player.isDead && player.isSneaking()) {
                ItemStack backpack = player.getCurrentArmor(2);
                if (backpack != null && backpack.getItem() instanceof ItemAdventureBackpack) {
                    mc.playerController.updateController();
                    if (player.getCurrentEquippedItem() != null) {
                        int slot = player.inventory.currentItem;
                        if (SlotTool.isValidTool(player.getCurrentEquippedItem())) {
                            AdventureBackpack.networkWrapper.sendToServer(new CycleToolMessage(dWheel, slot, true));
                            event.setCanceled(true);
                        }
                        if (player.getCurrentEquippedItem().getItem() instanceof ItemHose) {
                            AdventureBackpack.networkWrapper.sendToServer(new CycleToolMessage(-1, slot, false));
                            event.setCanceled(true);
                        }
                    }
                    mc.gameSettings.noclipRate += (float) event.dwheel * 0.25F;
                }
            }

        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void Suck(HoseSuckEvent event) {
        FluidStack result = Actions.attemptFill(event.world, event.target, event.entityPlayer, event.currentTank);
        if (result != null) {
            event.fluidResult = result;
            event.setResult(Event.Result.ALLOW);
        } else {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void Spill(HoseSpillEvent event) {
        FluidStack result = Actions.attemptPour(event.player, event.world, event.x, event.y, event.z, event.currentTank);
        if (result != null) {
            event.fluidResult = result;
            event.setResult(Event.Result.ALLOW);
        } else {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void playerDies(LivingDeathEvent event) {
        if (event.entity instanceof EntityPlayer && Wearing.isWearingBackpack((EntityPlayer) event.entity)) {
            Actions.tryPlaceOnDeath((EntityPlayer) event.entity);
        }
        event.setResult(Event.Result.ALLOW);
    }


}
