package script.theme_park.dungeon.mustafar_trials.old_republic_facility;

import script.library.prose;
import script.library.sui;
import script.*;

public class quest_object_06 extends script.base_script
{
    public quest_object_06()
    {
    }
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        obj_id building = getTopMostContainer(self);
        int status = 0;
        if (hasObjVar(building, "status"))
        {
            status = getIntObjVar(building, "status");
        }
        if (status < 8)
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("mustafar/old_republic_facility", "quest_object_06_use"));
        }
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            activateObject(self, player);
        }
        sendDirtyObjectMenuNotification(self);
        return SCRIPT_CONTINUE;
    }
    public void activateObject(obj_id self, obj_id player) throws InterruptedException
    {
        obj_id building = getTopMostContainer(self);
        int status = 0;
        if (hasObjVar(building, "status"))
        {
            status = getIntObjVar(building, "status");
        }
        if (status >= 8)
        {
            return;
        }
        setObjVar(building, "status", 8);
        obj_id smallroom31 = getCellId(building, "smallroom31");
        permissionsMakePublic(smallroom31);
        String title = "@mustafar/old_republic_facility:quest_object_06_sui_title";
        String prompt = "@mustafar/old_republic_facility:quest_object_06_sui_prompt";
        int pid = sui.msgbox(player, player, prompt, sui.OK_ONLY, title, "noHandler");
        sendSystemMessage(player, new string_id("mustafar/old_republic_facility", "quest_object_06_msg_self"));
        obj_id group = getGroupObject(player);
        if (isIdValid(group))
        {
            obj_id[] members = getGroupMemberIds(group);
            if (members == null || members.length == 0)
            {
                return;
            }
            for (int i = 0; i < members.length; i++)
            {
                if (members[i] != player)
                {
                    prose_package pp = new prose_package();
                    pp = prose.setStringId(pp, new string_id("mustafar/old_republic_facility", "quest_object_06_msg_other"));
                    pp = prose.setTU(pp, player);
                    sendSystemMessageProse(members[i], pp);
                }
            }
        }
        return;
    }
}
