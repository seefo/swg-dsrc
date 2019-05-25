package script.working.wwallace;

import script.*;
import script.base_class.*;
import script.combat_engine.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import script.base_script;

import script.library.utils;

public class space_resource_test extends script.base_script
{
    public space_resource_test()
    {
    }
    public static final String[] SPACE_RESOURCE_CONST = 
    {
        "space_chemical_acid",
        "space_chemical_cyanomethanic",
        "space_chemical_petrochem",
        "space_chemical_sulfuric",
        "space_gas_methane",
        "space_gas_organometallic",
        "space_gem_crystal",
        "space_gem_diamond",
        "space_metal_carbonaceous",
        "space_metal_ice",
        "space_metal_iron",
        "space_metal_obsidian",
        "space_metal_silicaceous"
    };
    public void makeResource(obj_id self, String rclass) throws InterruptedException
    {
        obj_id[] rtypes = getResourceTypes(rclass);
        sendSystemMessageTestingOnly(self, "Types are..." + rtypes[0].toString());
        obj_id rtype = rtypes[0];
        if (!isIdValid(rtype))
        {
            sendSystemMessageTestingOnly(self, "No id found");
            sendSystemMessageTestingOnly(self, "Type was " + rclass);
            return;
        }
        String crateTemplate = getResourceContainerForType(rtype);
        if (!crateTemplate.equals(""))
        {
            obj_id pInv = utils.getInventoryContainer(self);
            if (pInv != null)
            {
                obj_id crate = createObject(crateTemplate, pInv, "");
                if (addResourceToContainer(crate, rtype, 100000, self))
                {
                    sendSystemMessageTestingOnly(self, "Resource of class " + rclass + " added");
                }
            }
        }
    }
    public int OnSpeaking(obj_id self, String strText) throws InterruptedException
    {
        String[] strCommands = split(strText, ' ');
        if (strCommands[0].equals("createSpaceResource"))
        {
            String rclass = strCommands[1];
            makeResource(self, rclass);
        }
        else if (strCommands[0].equals("allSpaceResources"))
        {
            String[] resourceList = SPACE_RESOURCE_CONST;
            for (int i = 0; i < resourceList.length; i++)
            {
                makeResource(self, resourceList[i]);
            }
        }
        return SCRIPT_CONTINUE;
    }
}
