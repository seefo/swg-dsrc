package script.systems.crafting.item;

import script.dictionary;
import script.draft_schematic;
import script.library.craftinglib;
import script.library.permissions;
import script.obj_id;

public class crafting_base_ballot_terminal extends script.systems.crafting.crafting_base
{
    public crafting_base_ballot_terminal()
    {
    }
    public static final String VERSION = "v1.00.00";
    public void calcAndSetPrototypeProperties(obj_id prototype, draft_schematic.attribute[] itemAttributes, dictionary craftingValuesDictionary) throws InterruptedException
    {
        super.calcAndSetPrototypeProperties(prototype, itemAttributes, craftingValuesDictionary);
    }
    public void calcAndSetPrototypeProperties(obj_id prototype, draft_schematic.attribute[] itemAttributes) throws InterruptedException
    {
        debugServerConsoleMsg(null, "Beginning assembly-phase prototype property setting");
        for (int i = 0; i < itemAttributes.length; ++i)
        {
            if (itemAttributes[i] == null)
            {
                continue;
            }
            if (!calcAndSetPrototypeProperty(prototype, itemAttributes[i]))
            {
                if (((itemAttributes[i].name).getAsciiId()).equals("useModifier"))
                {
                    setObjVar(prototype, "crafting.stationMod", itemAttributes[i].currentValue);
                }
                else 
                {
                    setObjVar(prototype, craftinglib.COMPONENT_ATTRIBUTE_OBJVAR_NAME + "." + (itemAttributes[i].name).getAsciiId(), itemAttributes[i].currentValue);
                }
            }
        }
        permissions.initializePermissions(prototype);
        permissions.createPermissionsGroup(prototype, "administrators");
        permissions.grantGroupPermission(prototype, "administrators", permissions.canAdministrate);
    }
}
