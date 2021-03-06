package com.darkona.adventurebackpack.handlers;

import com.darkona.adventurebackpack.config.Keybindings;
import com.darkona.adventurebackpack.init.ModNetwork;
import com.darkona.adventurebackpack.network.CycleToolPacket;
import com.darkona.adventurebackpack.network.GUIPacket;
import com.darkona.adventurebackpack.network.MessageConstants;
import com.darkona.adventurebackpack.reference.Key;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Darkona on 11/10/2014.
 */
public class KeybindHandler
{

    private static Key getPressedKeyBinding()
    {
        if (Keybindings.openBackpack.isPressed())
        {
            return Key.OPEN_BACKPACK_INVENTORY;
        }
        if (Keybindings.toggleHose.isPressed())
        {
            return Key.TOGGLE_HOSE_TANK;
        }
        return Key.UNKNOWN;
    }

    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.KeyInputEvent event)
    {
        Key keypressed = getPressedKeyBinding();
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (keypressed == Key.OPEN_BACKPACK_INVENTORY)
        {
            if (mc.inGameHasFocus)
            {
                ModNetwork.net.sendToServer(new GUIPacket.GUImessage(MessageConstants.NORMAL_GUI, MessageConstants.FROM_KEYBIND));
            }
        }

        if (keypressed == Key.TOGGLE_HOSE_TANK)
        {
            ModNetwork.net.sendToServer(new CycleToolPacket.CycleToolMessage(0, (player).inventory.currentItem, CycleToolPacket.TOGGLE_HOSE_TANK));
        }
    }
}
