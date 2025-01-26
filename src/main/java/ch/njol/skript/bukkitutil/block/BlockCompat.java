package ch.njol.skript.bukkitutil.block;

import ch.njol.skript.aliases.ItemFlags;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Methods which operate with blocks but are not compatible across some
 * Minecraft versions.
 */
public interface BlockCompat {
	
	/**
	 * Instance of BlockCompat for current Minecraft version.
	 */
	BlockCompat INSTANCE = new NewBlockCompat();
	
	static final BlockSetter SETTER = INSTANCE.getSetter();

	/**
	 * Gets block values from a block state. They can be compared to other
	 * values if needed, but cannot be used to retrieve any other data.
	 * @param block Block state to retrieve value from.
	 * @return Block values.
	 * @deprecated Use {@link #getBlockValues(BlockData)} instead
	 */
	@Deprecated
	@Nullable
	BlockValues getBlockValues(BlockState block);
	
	/**
	 * Gets block values from a block. They can be compared to other values
	 * if needed, but cannot be used to retrieve any other data.
	 * @param block Block to retrieve value from.
	 * @return Block values.
	 */
	@Nullable
	default BlockValues getBlockValues(Block block) {
		return getBlockValues(block.getBlockData());
	}

	@Nullable
	BlockValues getBlockValues(Material material);

	@Nullable
	BlockValues getBlockValues(BlockData blockData);
	
	/**
	 * Gets block values from a item stack. They can be compared to other values
	 * if needed, but cannot be used to retrieve any other data.
	 * @param stack Item that would be placed as the block
	 * @return Block values.
	 */
	@Nullable
	BlockValues getBlockValues(ItemStack stack);

	/**
	 * Creates a block state from a falling block.
	 * @param entity Falling block entity
	 * @return Block state.
	 * @deprecated This shouldn't be used
	 */
	@Deprecated
	BlockState fallingBlockToState(FallingBlock entity);

	@Nullable
	default BlockValues getBlockValues(FallingBlock entity) {
		return getBlockValues(entity.getBlockData());
	}

	/**
	 * Creates new block values for given material and state. Item, if given,
	 * will be used to correct data value etc. when needed.
	 * @param type Block material.
	 * @param states Block states, as used in /setblock command in Minecraft.
	 * @param item Item form that may or may not provide additional
	 * information. Optional, but very useful on 1.12 and older.
	 * @param itemFlags Additional information about item. See {@link ItemFlags}.
	 * @return Block values, or null if given state was invalid.
	 */
	@Nullable
	BlockValues createBlockValues(Material type, Map<String, String> states, @Nullable ItemStack item, int itemFlags);
	
	/**
	 * Creates new block values for given material and state.
	 * @param type Block material.
	 * @param states Block states, as used in /setblock command in Minecraft.
	 * @return Block values, or null if given state was invalid.
	 */
	@Nullable
	default BlockValues createBlockValues(Material type, Map<String, String> states) {
		return createBlockValues(type, states, null, 0);
	}
	
	/**
	 * Gets block setter that understands block values produced by this
	 * compatibility layer.
	 * @return Block setter.
	 */
	BlockSetter getSetter();
		
	/**
	 * Checks whether the given material implies emptiness. On Minecraft 1.13+,
	 * there are several blocks that do so.
	 * @param type Material of block.
	 * @return Whether the material implies empty block.
	 */
	boolean isEmpty(Material type);
	
	/**
	 * Checks whether the given material is a liquid.
	 * @param type Material of block.
	 * @return Whether the material is liquid.
	 */
	boolean isLiquid(Material type);
}
