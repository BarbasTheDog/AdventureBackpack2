package com.darkona.adventurebackpack.fluids.effects;

import com.darkona.adventurebackpack.api.FluidEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by Darkona on 12/10/2014.
 */
public class OilEffect extends FluidEffect
{
    public OilEffect()
    {
        super(FluidRegistry.getFluid("oil"), 7, "Blerghhh!!");
    }

    /**
     * This method determines what will happen to the player when drinking the
     * corresponding fluid. For example set potion effects, set player on fire,
     * heal, fill hunger, etc. You can use the world parameter to make
     * conditions based on where the player is.
     *
     * @param world  The World.
     * @param player The Player.
     */
    @Override
    public void affectDrinker(World world, EntityPlayer player)
    {
        player.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 1, 2));
    }
}
