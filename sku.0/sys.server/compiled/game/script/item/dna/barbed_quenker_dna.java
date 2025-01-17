package script.item.dna;

import script.dictionary;
import script.library.*;
import script.location;
import script.obj_id;

public class barbed_quenker_dna extends script.base_script
{
    public barbed_quenker_dna()
    {
    }
    public static final String BARBED_QUENKER_DNA_LOOT_ITEM = "item_barbed_quenker_dna";
    public static final int BARBED_QUENKER_DNA_LOOT_CHANCE = 5;


    public int aiCorpsePrepared(obj_id self, dictionary params) throws InterruptedException
    {
        int chanceDna = rand(1, 100);
        if (chanceDna > BARBED_QUENKER_DNA_LOOT_CHANCE)
        {
            return SCRIPT_CONTINUE;
        }
        obj_id inv = utils.getInventoryContainer(self);
        obj_id dna = static_item.createNewItemFunction(BARBED_QUENKER_DNA_LOOT_ITEM, inv);
        return SCRIPT_CONTINUE;
    }
}
