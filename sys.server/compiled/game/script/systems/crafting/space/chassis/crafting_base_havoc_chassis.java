package script.systems.crafting.space.chassis;

import script.dictionary;
import script.draft_schematic;
import script.library.craftinglib;
import script.obj_id;

public class crafting_base_havoc_chassis extends script.systems.crafting.crafting_base
{
    public crafting_base_havoc_chassis()
    {
    }
    public void calcAndSetPrototypeProperties(obj_id prototype, draft_schematic.attribute[] itemAttributes, dictionary craftingValuesDictionary) throws InterruptedException
    {
        super.calcAndSetPrototypeProperties(prototype, itemAttributes, craftingValuesDictionary);
    }
    public void calcAndSetPrototypeProperties(obj_id prototype, draft_schematic.attribute[] itemAttributes) throws InterruptedException
    {
        debugServerConsoleMsg(null, "Beginning assembly-phase prototype property setting");
        setObjVar(prototype, "isShipDeed", true);
        setObjVar(prototype, "shiptype", "havoc");
        for (int i = 0; i < itemAttributes.length; ++i)
        {
            if (itemAttributes[i] == null)
            {
                continue;
            }
            if (!calcAndSetPrototypeProperty(prototype, itemAttributes[i]))
            {
                if (((itemAttributes[i].name).getAsciiId()).equals("massMax"))
                {
                    String OBJVAR_NAME = "ship_chassis.mass";
                    setObjVar(prototype, OBJVAR_NAME, (float)itemAttributes[i].currentValue);
                }
                if (((itemAttributes[i].name).getAsciiId()).equals("hp"))
                {
                    String OBJVAR_NAME = "ship_chassis.hp";
                    setObjVar(prototype, OBJVAR_NAME, (float)itemAttributes[i].currentValue);
                }
                else 
                {
                    setObjVar(prototype, craftinglib.COMPONENT_ATTRIBUTE_OBJVAR_NAME + "." + (itemAttributes[i].name).getAsciiId(), itemAttributes[i].currentValue);
                }
            }
        }
    }
}
