package com.darkona.adventurebackpack;

import com.darkona.adventurebackpack.api.FluidEffectRegistry;
import com.darkona.adventurebackpack.config.ConfigHandler;
import com.darkona.adventurebackpack.handlers.BackpackEventHandler;
import com.darkona.adventurebackpack.handlers.ClientEventHandler;
import com.darkona.adventurebackpack.handlers.GuiHandler;
import com.darkona.adventurebackpack.handlers.PlayerEventHandler;
import com.darkona.adventurebackpack.init.*;
import com.darkona.adventurebackpack.proxy.IProxy;
import com.darkona.adventurebackpack.reference.BackpackNames;
import com.darkona.adventurebackpack.reference.ModInfo;
import com.darkona.adventurebackpack.util.Utils;
import com.darkona.adventurebackpack.util.calendar.ChineseCalendar;
import com.darkona.adventurebackpack.util.calendar.JewishCalendar;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;

import java.util.Calendar;

/**
 * Created by Javier Darkona on 10/10/2014.
 */

@Mod(modid = ModInfo.MOD_ID,
        name = ModInfo.MOD_NAME,
        version = ModInfo.MOD_VERSION,
        guiFactory = ModInfo.GUI_FACTORY_CLASS)
public class AdventureBackpack
{

    @Mod.Instance(ModInfo.MOD_ID)
    public static AdventureBackpack instance;

    //Static things
    public static CreativeTabAB creativeTab = new CreativeTabAB();

    @SidedProxy(clientSide = ModInfo.MOD_CLIENT_PROXY, serverSide = ModInfo.MOD_SERVER_PROXY)
    public static IProxy proxy;
    public boolean chineseNewYear;
    public boolean hannukah;
    public String Holiday;
    PlayerEventHandler playerEventHandler;
    ClientEventHandler clientEventHandler;
    BackpackEventHandler backpackEventHandler;
    GuiHandler guiHandler;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {

        int year = Calendar.getInstance().get(Calendar.YEAR), month = Calendar.getInstance().get(Calendar.MONTH), day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        //Configuration
        FMLCommonHandler.instance().bus().register(new ConfigHandler());
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        chineseNewYear = ChineseCalendar.isChineseNewYear(year,month,day);
        hannukah = JewishCalendar.isHannukah(year, month, day);
        Holiday = Utils.getHoliday();
        //ModStuff
        ModItems.init();
        ModBlocks.init();
        ModFluids.init();
        FluidEffectRegistry.init();

        ModEntities.init();
        ModNetwork.init();
        proxy.initNetwork();
        // EVENTS
        playerEventHandler = new PlayerEventHandler();
        backpackEventHandler = new BackpackEventHandler();
        clientEventHandler = new ClientEventHandler();
        MinecraftForge.EVENT_BUS.register(backpackEventHandler);
        MinecraftForge.EVENT_BUS.register(clientEventHandler);
        MinecraftForge.EVENT_BUS.register(playerEventHandler);
        FMLCommonHandler.instance().bus().register(playerEventHandler);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {

        proxy.init();
        ModRecipes.init();
        ModWorldGen.init();
        //GUIs
        guiHandler = new GuiHandler();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, guiHandler);

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

    }

}
