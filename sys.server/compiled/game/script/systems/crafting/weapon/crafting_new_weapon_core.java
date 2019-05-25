package script.systems.crafting.weapon;

import script.dictionary;
import script.draft_schematic;
import script.library.craftinglib;
import script.library.weapons;
import script.obj_id;

public class crafting_new_weapon_core extends script.systems.crafting.crafting_base
{
    public crafting_new_weapon_core()
    {
    }
    public static final String VERSION = "v0.00.00";
    public void calcAndSetPrototypeProperties(obj_id prototype, draft_schematic.attribute[] itemAttributes, dictionary craftingValuesDictionary) throws InterruptedException
    {
        super.calcAndSetPrototypeProperties(prototype, itemAttributes, craftingValuesDictionary);
    }
    public void calcAndSetPrototypeProperties(obj_id prototype, draft_schematic.attribute[] itemAttributes) throws InterruptedException
    {
        obj_id self = getSelf();
        for (int i = 0; i < itemAttributes.length; ++i)
        {
            if (itemAttributes[i] == null)
            {
                continue;
            }
            if (!calcAndSetPrototypeProperty(prototype, itemAttributes[i]))
            {
                if (itemAttributes[i].currentValue < itemAttributes[i].minValue)
                {
                    itemAttributes[i].currentValue = itemAttributes[i].minValue;
                }
                else if (itemAttributes[i].currentValue > itemAttributes[i].maxValue)
                {
                    itemAttributes[i].currentValue = itemAttributes[i].maxValue;
                }
                setObjVar(prototype, craftinglib.COMPONENT_ATTRIBUTE_OBJVAR_NAME + "." + (itemAttributes[i].name).getAsciiId(), itemAttributes[i].currentValue);
                setObjVar(prototype, weapons.OBJVAR_NEW_WP_COMPONENT, true);
                weapons.setComponentObjVars(prototype, itemAttributes);
            }
        }
        weapons.setSchematicVariablesFromProtoType(self, prototype);
    }
}
