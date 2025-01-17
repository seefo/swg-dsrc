package script.working.cmayer;

import script.dictionary;
import script.location;
import script.obj_id;

public class buildingtest extends script.base_script
{
    public buildingtest()
    {
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        createTriggerVolume("guards", 20f, true);
        obj_id room = getCellId(self, "mainhall");
        location termLoc = new location(0.1f, .13f, -5.46f, "tatooine", room);
        obj_id destructor = createObject("object/tangible/furniture/imperial/data_terminal_s1.iff", termLoc);
        setObjVar(self, "DESTRUCTOR", destructor);
        setObjVar(destructor, "bldg", self);
        attachScript(destructor, "theme_park.bldg_destroy");
        return SCRIPT_CONTINUE;
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        createTriggerVolume("guards", 20f, true);
        obj_id room = getCellId(self, "mainhall");
        location termLoc = new location(0.1f, .13f, -5.46f, "tatooine", room);
        obj_id destructor = createObject("object/tangible/furniture/imperial/data_terminal_s1.iff", termLoc);
        setObjVar(self, "DESTRUCTOR", destructor);
        setObjVar(destructor, "bldg", self);
        attachScript(destructor, "theme_park.bldg_destroy");
        return SCRIPT_CONTINUE;
    }
    public int OnTriggerVolumeEntered(obj_id self, String volumeName, obj_id breacher) throws InterruptedException
    {
        if (!isPlayer(breacher))
        {
            return SCRIPT_CONTINUE;
        }
        if (volumeName.equals("guards"))
        {
            obj_id g1 = getObjIdObjVar(self, "guard1");
            obj_id g2 = getObjIdObjVar(self, "guard2");
            obj_id g3 = getObjIdObjVar(self, "guard3");
            obj_id g4 = getObjIdObjVar(self, "guard4");
            startCombat(g1, breacher);
            startCombat(g2, breacher);
            startCombat(g3, breacher);
            startCombat(g4, breacher);
        }
        return SCRIPT_CONTINUE;
    }
    public int spawnTerminal(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id room = getCellId(self, "mainhall");
        location termLoc = new location(0.1f, .13f, -5.46f, "tatooine", room);
        obj_id destructor = createObject("object/tangible/furniture/imperial/data_terminal_s1.iff", termLoc);
        setObjVar(self, "DESTRUCTOR", destructor);
        setObjVar(destructor, "bldg", self);
        attachScript(destructor, "theme_park.bldg_destroy");
        return SCRIPT_CONTINUE;
    }
}
