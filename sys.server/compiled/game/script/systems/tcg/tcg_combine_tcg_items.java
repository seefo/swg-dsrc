package script.systems.tcg;

import script.library.static_item;
import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

import java.util.Vector;

public class tcg_combine_tcg_items extends script.base_script
{
    public tcg_combine_tcg_items()
    {
    }
    public static final boolean LOGGING_ON = true;
    public static final String LOGGING_CATEGORY = "combine";
    public static final String TEMPLATE_PATTERN = "object/tangible/tcg/series3/combine_object_";
    public static final String COMBINE = "combine";
    public static final String JET_PACK_DEED = "item_tcg_merr_sonn_jt12_jetpack_deed";
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        blog("combine: OnObjectMenuRequest - Init");
        if (!utils.isNestedWithinAPlayer(self))
        {
            return SCRIPT_CONTINUE;
        }
        blog("combine: OnObjectMenuRequest - Is Nested");
        obj_id owner = utils.getContainingPlayer(self);
        if (!isValidId(owner) || !exists(owner))
        {
            return SCRIPT_CONTINUE;
        }
        if (owner != player)
        {
            return SCRIPT_CONTINUE;
        }
        blog("combine: OnObjectMenuRequest - Owner: " + owner);
        if (hasObjVar(self, COMBINE) && (getStringObjVar(self, COMBINE)).startsWith("true"))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("tcg", "combine_tcg_items"));
        }
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item != menu_info_types.ITEM_USE)
        {
            return SCRIPT_CONTINUE;
        }
        if (!utils.isNestedWithinAPlayer(self))
        {
            return SCRIPT_CONTINUE;
        }
        blog("combine: OnObjectMenuSelect - Is Nested");
        obj_id owner = utils.getContainingPlayer(self);
        if (!isValidId(owner) || !exists(owner))
        {
            return SCRIPT_CONTINUE;
        }
        if (owner != player)
        {
            return SCRIPT_CONTINUE;
        }
        blog("combine: OnObjectMenuSelect - Owner: " + owner);
        obj_id[] allPossibleMatches = utils.getAllItemsPlayerHasByTemplateStartsWith(owner, TEMPLATE_PATTERN);
        if (allPossibleMatches.length < 4)
        {
            sendSystemMessage(player, new string_id("tcg", "combination_not_enough_items"));
            return SCRIPT_CONTINUE;
        }
        blog("combine: OnObjectMenuSelect - allPossibleMatches.length: " + allPossibleMatches.length);
        obj_id[] combinableObjects = getListOfCombinableObjects(allPossibleMatches);
        if (combinableObjects == null || combinableObjects.length <= 0)
        {
            CustomerServiceLog("tcg", "Jet Pack Combination Attempt Failed for: " + owner + " " + getPlayerName(owner) + ", reason: not enough combinable objects in inventory.");
            sendSystemMessage(player, new string_id("tcg", "combination_not_enough_items"));
            return SCRIPT_CONTINUE;
        }
        blog("combine: OnObjectMenuSelect - combinableObjects.length: " + combinableObjects.length);
        if (!getCorrectCombination(combinableObjects, owner))
        {
            sendSystemMessage(player, new string_id("tcg", "combination_failed"));
            return SCRIPT_CONTINUE;
        }
        sendSystemMessage(player, new string_id("tcg", "combination_success"));
        blog("combine: OnObjectMenuSelect - SUCCESS");
        return SCRIPT_CONTINUE;
    }
    public obj_id[] getListOfCombinableObjects(obj_id[] allPossibleMatches) throws InterruptedException
    {
        blog("combine: getListOfCombinableObjects - init");
        if (allPossibleMatches == null || allPossibleMatches.length <= 0)
        {
            return null;
        }
        Vector combinableObjects = new Vector();
        combinableObjects.setSize(0);
        for (int i = 0; i < allPossibleMatches.length; i++)
        {
            if (hasObjVar(allPossibleMatches[i], COMBINE) && (getStringObjVar(allPossibleMatches[i], COMBINE)).startsWith("true"))
            {
                utils.addElement(combinableObjects, allPossibleMatches[i]);
            }
        }
        if (combinableObjects.size() < 4)
        {
            return null;
        }
        blog("combine: getListOfCombinableObjects - List longer or equal to 4");
        return utils.toStaticObjIdArray(combinableObjects);
    }
    public boolean getCorrectCombination(obj_id[] combinableObjects, obj_id owner) throws InterruptedException
    {
        blog("combine: getCorrectCombination - init");
        if (combinableObjects == null || combinableObjects.length <= 0)
        {
            return false;
        }
        if (!isValidId(owner) || !exists(owner))
        {
            return false;
        }
        boolean bobaFound = false;
        boolean jangoFound = false;
        boolean bannerFound = false;
        boolean bluePrintFound = false;
        Vector toBeCombinedList = new Vector();
        toBeCombinedList.setSize(0);
        for (int j = 0; j < combinableObjects.length; j++)
        {
            if (!jangoFound && (getTemplateName(combinableObjects[j])).equals("object/tangible/tcg/series3/combine_object_jango_fett_memorial_statue.iff"))
            {
                utils.addElement(toBeCombinedList, combinableObjects[j]);
                if (owner != utils.getContainingPlayer(combinableObjects[j]))
                {
                    break;
                }
                jangoFound = true;
                CustomerServiceLog("tcg", "Jet Pack Combination - Player: " + owner + " " + getPlayerName(owner) + " has Jango Fett object (" + combinableObjects[j] + ")in inventory");
            }
            else if (!bobaFound && (getTemplateName(combinableObjects[j])).equals("object/tangible/tcg/series3/combine_object_boba_fett_statue.iff"))
            {
                utils.addElement(toBeCombinedList, combinableObjects[j]);
                if (owner != utils.getContainingPlayer(combinableObjects[j]))
                {
                    break;
                }
                bobaFound = true;
                CustomerServiceLog("tcg", "Jet Pack Combination - Player: " + owner + " " + getPlayerName(owner) + " has Boba Fett object (" + combinableObjects[j] + ")in inventory");
            }
            else if (!bannerFound && (getTemplateName(combinableObjects[j])).equals("object/tangible/tcg/series3/combine_object_mandalorian_skull_banner.iff"))
            {
                utils.addElement(toBeCombinedList, combinableObjects[j]);
                if (owner != utils.getContainingPlayer(combinableObjects[j]))
                {
                    break;
                }
                bannerFound = true;
                CustomerServiceLog("tcg", "Jet Pack Combination - Player: " + owner + " " + getPlayerName(owner) + " has Madalorian Banner object (" + combinableObjects[j] + ")in inventory");
            }
            else if (!bluePrintFound && (getTemplateName(combinableObjects[j])).equals("object/tangible/tcg/series3/combine_object_merr_sonn_jt12_jetpack_blueprints.iff"))
            {
                utils.addElement(toBeCombinedList, combinableObjects[j]);
                if (owner != utils.getContainingPlayer(combinableObjects[j]))
                {
                    break;
                }
                bluePrintFound = true;
                CustomerServiceLog("tcg", "Jet Pack Combination - Player: " + owner + " " + getPlayerName(owner) + " has Merr-Sonn Blueprint object (" + combinableObjects[j] + ")in inventory");
            }
        }
        if (toBeCombinedList.size() < 4)
        {
            CustomerServiceLog("tcg", "Jet Pack Combination Failed - Player: " + owner + " " + getPlayerName(owner) + " has only: " + toBeCombinedList.size() + " items that can be combined.");
            return false;
        }
        if (bobaFound && jangoFound && bannerFound && bluePrintFound)
        {
            CustomerServiceLog("tcg", "Jet Pack Combination - Player: " + owner + " " + getPlayerName(owner) + " has all 4 items needed to get jet pack. Green light for combination process.");
            obj_id[] combinableList = utils.toStaticObjIdArray(toBeCombinedList);
            return combineTheCombinableObjects(combinableList, owner);
        }
        return false;
    }
    public boolean combineTheCombinableObjects(obj_id[] combinableList, obj_id owner) throws InterruptedException
    {
        blog("combine: combineTheCombinableObjects - init");
        if (!isValidId(owner) || !exists(owner))
        {
            CustomerServiceLog("tcg", "Jet Pack Combination Failed - Last process needed for combine function received a NULL for owner.");
            return false;
        }
        if (combinableList == null || combinableList.length <= 0)
        {
            CustomerServiceLog("tcg", "Jet Pack Combination Failed - Last process needed for combine function received a NULL combinableList for Player: " + owner + " " + getPlayerName(owner) + ".");
            return false;
        }
        if (combinableList.length != 4)
        {
            CustomerServiceLog("tcg", "Jet Pack Combination Failed - Last process needed for combine function received a combinableList of less than 4 items for Player: " + owner + " " + getPlayerName(owner) + ".");
            return false;
        }
        int checkSum = 0;
        for (int i = 0; i < combinableList.length; i++)
        {
            if (!isValidId(combinableList[i]) || !exists(combinableList[i]))
            {
                CustomerServiceLog("tcg", "Jet Pack Combination ERROR - Object (" + combinableList[i] + ") No longer exists for combine process. Possible attempt to exploit involving Player: " + owner + " " + getPlayerName(owner) + ".");
                continue;
            }
            if (owner != utils.getContainingPlayer(combinableList[i]))
            {
                CustomerServiceLog("tcg", "Jet Pack Combination ERROR - Object (" + combinableList[i] + ") No longer exists in player inventory for the combine process. Possible attempt to exploit involving Player: " + owner + " " + getPlayerName(owner) + ".");
                continue;
            }
            if (!hasObjVar(combinableList[i], COMBINE) && (getStringObjVar(combinableList[i], COMBINE)).startsWith("true"))
            {
                CustomerServiceLog("tcg", "Jet Pack Combination ERROR - Object (" + combinableList[i] + ") No longer has objvar allowing combination process. Possible attempt to exploit involving Player: " + owner + " " + getPlayerName(owner) + ".");
                continue;
            }
            checkSum++;
        }
        if (checkSum != 4)
        {
            CustomerServiceLog("tcg", "Jet Pack Combination Failed - One or more objects failed to be combined. This may be due to a catestrophic error or Player: " + owner + " " + getPlayerName(owner) + " attempting to subvert/exploit systems.");
            return false;
        }
        obj_id playerInv = utils.getInventoryContainer(owner);
        if (!isValidId(playerInv) || !exists(playerInv))
        {
            CustomerServiceLog("tcg", "Jet Pack Combination Failed - Vehicle Deed creation failed for for Player: " + owner + " " + getPlayerName(owner) + ". Reason: Player Inventory invalid.");
            return false;
        }
        obj_id deed = static_item.createNewItemFunction(JET_PACK_DEED, playerInv, null);
        if (!isValidId(deed) || !exists(deed))
        {
            CustomerServiceLog("tcg", "Jet Pack Combination Failed - Vehicle Deed creation failed for for Player: " + owner + " " + getPlayerName(owner) + ". Reason: Deed could not be created in Player Inventory.");
            return false;
        }
        CustomerServiceLog("tcg", "Jet Pack Combination Success - Jet Pack Deed creation success. Deed Object: (" + deed + ") in player inventory: " + playerInv + " for Player: " + owner + " " + getPlayerName(owner) + ".");
        for (int j = 0; j < combinableList.length; j++)
        {
            if (!isValidId(combinableList[j]) || !exists(combinableList[j]))
            {
                CustomerServiceLog("tcg", "Jet Pack Combination ERROR - Combine object: " + combinableList[j] + " could not be found for update. This object was last owned by Player: " + owner + " " + getPlayerName(owner) + " and needed to be updated to avoid reuse in the combination process.");
                continue;
            }
            setObjVar(combinableList[j], COMBINE, "false");
            CustomerServiceLog("tcg", "Jet Pack Combination - Combine object: " + combinableList[j] + " was successfully updated to remove objvar. This object should no longer allow for combination functionality. Object owned by Player: " + owner + " " + getPlayerName(owner) + ".");
        }
        CustomerServiceLog("tcg", "Jet Pack Combination SUCCESS - All objects combined for Player: " + owner + " " + getPlayerName(owner) + ". Deed object: " + deed + " created in player inventory, objects used in process should be updated.");
        return true;
    }
    public boolean blog(String msg) throws InterruptedException
    {
        if (LOGGING_ON && msg != null && !msg.equals(""))
        {
            LOG(LOGGING_CATEGORY, msg);
        }
        return true;
    }
}
