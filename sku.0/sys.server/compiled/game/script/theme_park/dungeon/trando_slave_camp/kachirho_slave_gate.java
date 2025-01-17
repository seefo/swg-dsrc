package script.theme_park.dungeon.trando_slave_camp;

import script.*;
import script.library.groundquests;
import script.library.space_dungeon;
import script.library.space_dungeon_data;
import script.library.utils;

public class kachirho_slave_gate extends script.base_script
{
    public kachirho_slave_gate()
    {
    }
    public static final string_id SID_UNABLE_TO_FIND_DUNGEON = new string_id("dungeon/space_dungeon", "unable_to_find_dungeon");
    public static final string_id SID_NO_TICKET = new string_id("dungeon/space_dungeon", "no_ticket");
    public static final string_id SID_REQUEST_TRAVEL = new string_id("dungeon/space_dungeon", "request_travel");
    public static final string_id SID_REQUEST_TRAVEL_OUTSTANDIN = new string_id("dungeon/space_dungeon", "request_travel_outstanding");
    public static final string_id SID_SLAVER_CAMP = new string_id("travel/zone_transition", "kachirho_trando_slave_camp");
    public static final string_id SID_NO_PERMISSION = new string_id("travel/zone_transition", "default_no_access");
    public static final String QUEST = "ep3_slaver_gursan_entry_quest";
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info item) throws InterruptedException
    {
        if (getDistance(player, self) > 6.0f)
        {
            return SCRIPT_CONTINUE;
        }
        utils.dismountRiderJetpackCheck(player);
        if (!hasObjVar(self, "space_dungeon.ticket.point"))
        {
            setObjVar(self, "space_dungeon.ticket.point", "kashyyyk_main");
        }
        if (!hasObjVar(self, "space_dungeon.ticket.dungeon"))
        {
            setObjVar(self, "space_dungeon.ticket.dungeon", "trando_slave_camp");
        }
        item.addRootMenu(menu_info_types.ITEM_USE, SID_SLAVER_CAMP);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (groundquests.hasCompletedQuest(player, QUEST) && !groundquests.hasCompletedQuest(player, "ep3_kymayrr_send_cyssc_signal"))
            {
                space_dungeon.sendGroupToDungeonWithoutTicket(player, "trando_slave_camp", "kashyyyk_main", "kashyyyk_main", "slaveCampGeneric", self);
                return SCRIPT_CONTINUE;
            }
            if (!groundquests.isQuestActive(player, QUEST))
            {
                sendSystemMessage(player, SID_NO_PERMISSION);
                return SCRIPT_CONTINUE;
            }
            if (groundquests.isTaskActive(player, QUEST, "slaverEnterGate"))
            {
                groundquests.sendSignal(player, "signalSlaverEnterGate");
                return SCRIPT_CONTINUE;
            }
            if (groundquests.isTaskActive(player, QUEST, "slaverCampEntered"))
            {
                space_dungeon.sendGroupToDungeonWithoutTicket(player, "trando_slave_camp", "kashyyyk_main", "kashyyyk_main", "slaveCampGeneric", self);
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int OnClusterWideDataResponse(obj_id self, String manage_name, String dungeon_type, int request_id, String[] element_name_list, dictionary[] dungeon_data, int lock_key) throws InterruptedException
    {
        LOG("space_dungeon", "travel_space_dungeon.OnClusterWideDataResponse");
        obj_id player = space_dungeon.getDungeonTraveler(self, request_id);
        if (!isIdValid(player) || !player.isAuthoritative())
        {
            LOG("space_dungeon", "travel_space_dungeon.OnClusterWideDataResponse -- player is invalid or not authoritative.");
            releaseClusterWideDataLock(manage_name, lock_key);
            if (isIdValid(player))
            {
                space_dungeon.cleanupPlayerTicketObjvars(player);
            }
            return SCRIPT_CONTINUE;
        }
        obj_id ticket = null;
        if (hasObjVar(player, space_dungeon.VAR_TICKET_USED))
        {
            ticket = getObjIdObjVar(player, space_dungeon.VAR_TICKET_USED);
        }
        else 
        {
            ticket = player;
        }
        if (!isIdValid(ticket) || !ticket.isAuthoritative())
        {
            sendSystemMessage(player, space_dungeon.SID_ILLEGAL_TICKET);
            space_dungeon.cleanupPlayerTicketObjvars(player);
            releaseClusterWideDataLock(manage_name, lock_key);
            return SCRIPT_CONTINUE;
        }
        if (!manage_name.equals("dungeon"))
        {
            LOG("space_dungeon", "travel_space_dungeon.OnClusterWideDataResponse -- ignorning manage_name " + manage_name + " because it is not dungeon.");
            space_dungeon.cleanupPlayerTicketObjvars(player);
            releaseClusterWideDataLock(manage_name, lock_key);
            return SCRIPT_CONTINUE;
        }
        if (request_id < 1)
        {
            LOG("space_dungeon", "travel_space_dungeon.OnClusterWideDataResponse -- invalid request_id value of " + request_id);
            space_dungeon.cleanupPlayerTicketObjvars(player);
            releaseClusterWideDataLock(manage_name, lock_key);
            return SCRIPT_CONTINUE;
        }
        if (dungeon_data == null)
        {
            LOG("space_dungeon", "travel_space_dungeon.OnClusterWideDataResponse -- dungeon_data is null.");
            space_dungeon.cleanupPlayerTicketObjvars(player);
            releaseClusterWideDataLock(manage_name, lock_key);
            return SCRIPT_CONTINUE;
        }
        if (element_name_list == null)
        {
            LOG("space_dungeon", "travel_space_dungeon.OnClusterWideDataResponse -- element_name_list is null.");
            space_dungeon.cleanupPlayerTicketObjvars(player);
            releaseClusterWideDataLock(manage_name, lock_key);
            return SCRIPT_CONTINUE;
        }
        if (dungeon_type == null)
        {
            LOG("space_dungeon", "travel_space_dungeon.OnClusterWideDataResponse -- dungeon_type is null.");
            space_dungeon.cleanupPlayerTicketObjvars(player);
            releaseClusterWideDataLock(manage_name, lock_key);
            return SCRIPT_CONTINUE;
        }
        String dungeon_name = dungeon_type.substring(0, dungeon_type.length() - 1);
        for (int i = 0; i < dungeon_data.length; i++)
        {
            if (false == space_dungeon_data.isValidDungeon(dungeon_name))
            {
                LOG("space_dungeon", "travel_space_dungeon.OnClusterWideDataResponse -- dungeon name of " + dungeon_name + " is not in the dungeon datatable.");
                break;
            }
            dictionary dungeon = dungeon_data[i];
            obj_id dungeon_id = dungeon.getObjId("dungeon_id");
            int session_id = dungeon.getInt("session_id");
            LOG("space_dungeon", "travel_space_dungeon.OnClusterWideDataResponse -- session_id ->" + session_id + " dungeon_instance ->" + element_name_list[i]);
            if (!isIdValid(dungeon_id))
            {
                LOG("space_dungeon", "travel_space_dungeon.OnClusterWideDataResponse -- bad data found for dungeon entry " + i + ". Ignoring.");
                continue;
            }
            if (session_id < 1)
            {
                setObjVar(player, space_dungeon.VAR_SESSION_ID, lock_key);
                dictionary dungeon_update = new dictionary();
                dungeon_update.put("session_id", lock_key);
                updateClusterWideData(manage_name, element_name_list[i], dungeon_update, lock_key);
                releaseClusterWideDataLock(manage_name, lock_key);
                dictionary d = new dictionary();
                d.put("session_id", lock_key);
                d.put("request_id", request_id);
                d.put("player", player);
                d.put("ticket_collector", self);
                if (hasObjVar(ticket, space_dungeon.VAR_TICKET_QUEST_TYPE))
                {
                    d.put("quest_type", getStringObjVar(ticket, space_dungeon.VAR_TICKET_QUEST_TYPE));
                }
                messageTo(dungeon_id, "msgSetSessionId", d, 0.0f, false);
                return SCRIPT_CONTINUE;
            }
        }
        releaseClusterWideDataLock(manage_name, lock_key);
        space_dungeon.cleanupPlayerTicketObjvars(player);
        space_dungeon.removeDungeonTraveler(self, request_id);
        string_id success = space_dungeon_data.getDungeonFailureString(dungeon_name);
        if (success == null)
        {
            sendSystemMessage(player, SID_UNABLE_TO_FIND_DUNGEON);
        }
        else 
        {
            sendSystemMessage(player, success);
        }
        return SCRIPT_CONTINUE;
    }
    public int msgStartDungeonTravel(obj_id self, dictionary params) throws InterruptedException
    {
        int session_id = params.getInt("session_id");
        LOG("space_dungeon", "msgStartDungeonTravel -- session_id ->" + session_id);
        obj_id dungeon_id = params.getObjId("dungeon_id");
        if (!isIdValid(dungeon_id))
        {
            LOG("space_dungeon", "player_travel.msgStartDungeonTravel -- dungeon_id is invalid for " + self + ".");
            return SCRIPT_CONTINUE;
        }
        String dungeon_name = params.getString("dungeon_name");
        if (dungeon_id == null)
        {
            LOG("space_dungeon", "player_travel.msgStartDungeonTravel -- dungeon_name is null for " + self + ".");
            return SCRIPT_CONTINUE;
        }
        int request_id = params.getInt("request_id");
        obj_id player = space_dungeon.getDungeonTraveler(self, request_id);
        if (!isIdValid(player))
        {
            LOG("space_dungeon", "player_travel.msgStartDungeonTravel -- player is null for " + self);
            return SCRIPT_CONTINUE;
        }
        space_dungeon.removeDungeonTraveler(self, request_id);
        if (player.isAuthoritative())
        {
            location dungeon_loc = params.getLocation("dungeon_loc");
            if (dungeon_loc == null)
            {
                LOG("space_dungeon", "travel_space_dungeon.msgStartDungeonTravel -- location is null for " + self + ".");
                return SCRIPT_CONTINUE;
            }
            space_dungeon.movePlayerGroupToDungeon(player, dungeon_id, dungeon_name, dungeon_loc);
        }
        return SCRIPT_CONTINUE;
    }
}
