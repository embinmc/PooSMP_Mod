package embin.poosmp.util;

import net.minecraft.nbt.CompoundTag;

@Deprecated(forRemoval = true)
public class MoneyData {
    public static int add_money(IEntityDataSaver player, int amount) {
        CompoundTag nbt = player.poosmpmod$getPersistentData();
        int money = nbt.getIntOr("money", 0);
        money += amount;
        if (money >= 2_147_000_000) {
            money = 2_147_000_000;
        }
        nbt.putInt("money", money);
        return money;
    }

    public static int remove_money(IEntityDataSaver player, int amount) {
        CompoundTag nbt = player.poosmpmod$getPersistentData();
        int money = nbt.getIntOr("money", 0);
        money -= amount;
        if (money <= 0) {
            money = 0;
        }
        nbt.putInt("money", money);
        return money;
    }
}
