package com.darkona.adventurebackpack.init;

import com.darkona.adventurebackpack.init.recipes.AbstractBackpackRecipeTwo;
import com.darkona.adventurebackpack.init.recipes.BackpackRecipes;
import com.darkona.adventurebackpack.init.recipes.BackpackRecipesList;
import com.darkona.adventurebackpack.reference.BackpackNames;
import com.darkona.adventurebackpack.reference.ModInfo;
import com.darkona.adventurebackpack.util.LogHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.lang.reflect.Field;

/**
 * Created on 20/10/2014
 *
 * @author Darkona
 */
public class ModRecipes
{
    private static ItemStack bc(int damage)
    {
        return BackpackNames.setBackpackColorNameFromDamage(new ItemStack(ModItems.adventureBackpack),damage);
    }

    public static void init()
    {

        //Sleeping Bag - temporal recipe
        GameRegistry.addRecipe(new ItemStack(ModItems.component, 1, 1),
                "  X",
                "CCC",
                'X', Blocks.wool,
                'C', Blocks.carpet
        );

        //Backpack Tank
        GameRegistry.addRecipe(new ItemStack(ModItems.component, 1, 2),
                "GIG",
                "GGG",
                "GIG",
                'G', Blocks.glass,
                'I', Items.iron_ingot
        );

        //Hose Nozzle
        GameRegistry.addRecipe(new ItemStack(ModItems.component, 1, 3),
                " G ",
                "ILI",
                "   ",
                'G', Items.gold_ingot,
                'I', Items.iron_ingot,
                'L', Blocks.lever
        );

        //Machete Handle
        GameRegistry.addRecipe(new ItemStack(ModItems.component, 1, 4),
                "YIY",
                "BSB",
                "RbR",
                'Y', new ItemStack(Items.dye, 1, 11),
                'B', new ItemStack(Items.dye, 1, 4),
                'R', new ItemStack(Items.dye, 1, 1),
                'b', new ItemStack(Items.dye, 1, 0),
                'I', Items.iron_ingot,
                'S', Items.stick
        );

        GameRegistry.addRecipe(new ItemStack(ModItems.machete),
                " I ",
                " I ",
                " H ",
                'I', Items.iron_ingot,
                'H', new ItemStack(ModItems.component, 1, 4)
        );

        GameRegistry.addRecipe(new ItemStack(ModItems.adventureHat),
                "   ",
                "nC ",
                "LLL",
                'n', Items.gold_nugget,
                'C', Items.leather_helmet,
                'L', Items.leather
        );

        GameRegistry.addRecipe(new ItemStack(ModItems.adventureSuit),
                " V ",
                " W ",
                " P ",
                'V', Items.leather_chestplate,
                'W', Blocks.wool,
                'P', Items.leather_leggings
        );

        GameRegistry.addRecipe(new ItemStack(ModItems.pistonBoots),
                " B ",
                "PSP",
                'B', Items.leather_boots,
                'P', Blocks.piston,
                'S', Items.slime_ball
        );

        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.melonJuiceBottle),
                Items.melon, Items.potionitem
        );

        GameRegistry.addRecipe(new ItemStack(ModItems.hose),
                "NGG",
                "  G",
                "  G",
                'N', new ItemStack(ModItems.component, 1, 3),
                'G', new ItemStack(Items.dye, 1, 2)
        );

        /*GameRegistry.addRecipe(a,
                "LGL",
                "TCT",
                "LSL",
                'L', Items.leather,
                'G', Items.gold_ingot,
                'T', new ItemStack(ModItems.component, 1, 2),
                'S', new ItemStack(ModItems.component, 1, 1),
                'C', Blocks.chest
        );*/

        BackpackRecipesList br = new BackpackRecipesList();
        int counter = 0;
        for (int i = 0; i < BackpackNames.backpackNames.length; i++)
        {
            for (Field field : BackpackRecipesList.class.getFields())
            {
                try
                {
                    if (field.getName().equals((BackpackNames.backpackNames[i])))
                    {
                        GameRegistry.addRecipe(new ShapedOreRecipe(BackpackNames.setBackpackColorNameFromDamage(new ItemStack(ModItems.adventureBackpack), i), (Object[]) field.get(br)));
                        counter++;
                    }
                } catch (Exception oops)
                {
                    LogHelper.error("Huge mistake during reflection. Some bad things might happen: " + oops.getClass().getName());
                    oops.printStackTrace();
                }
            }

        }
        LogHelper.info("Loaded " + counter + " backpack recipes.");

            //GameRegistry.addRecipe(new AbstractBackpackRecipe());
        /*BackpackRecipes br = new BackpackRecipes();
        int i = 0;
        for (Field field : BackpackRecipes.class.getFields())
        {
            try
            {
                if (field.getType() == ItemStack[].class)
                {
                    AbstractBackpackRecipeTwo recipe = new AbstractBackpackRecipeTwo(field.getName(), (ItemStack[]) field.get(br));
                    GameRegistry.addRecipe(recipe);
                    //LogHelper.info("Loaded recipe for " + field.getName() + " backpack.");
                    i++;
                }
            } catch (Exception oops)
            {
                LogHelper.error("Huge mistake during reflection. Some bad things might happen.");
            }
        }
        LogHelper.info("Loaded " + i + " backpack recipes.");
        RecipeSorter.register(ModInfo.MOD_ID + ":adventureBackpack", AbstractBackpackRecipeTwo.class, RecipeSorter.Category.SHAPED, "after:minecraft:shapeless");
        */
    }
}
