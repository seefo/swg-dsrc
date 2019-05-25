package script.working.rdelashmit;

import script.*;
import script.base_class.*;
import script.combat_engine.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import script.base_script;

public class city_test extends script.base_script
{
    public city_test()
    {
    }
    public obj_id getPlayerByName(obj_id actor, String name) throws InterruptedException
    {
        java.util.StringTokenizer st = new java.util.StringTokenizer(name);
        String compareName = toLower(st.nextToken());
        obj_id[] players = getPlayerCreaturesInRange(actor, 128.0f);
        if (players != null)
        {
            for (int i = 0; i < players.length; ++i)
            {
                java.util.StringTokenizer st2 = new java.util.StringTokenizer(getName(players[i]));
                String playerName = toLower(st2.nextToken());
                if (compareName.equals(playerName))
                {
                    return players[i];
                }
            }
        }
        return obj_id.NULL_ID;
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        debugSpeakMsg(self, "city test script attached.");
        return SCRIPT_CONTINUE;
    }
    public int OnCityChanged(obj_id self, int oldCityId, int newCityId) throws InterruptedException
    {
        if (oldCityId != 0)
        {
            debugSpeakMsg(self, "Leaving city " + cityGetName(oldCityId));
        }
        if (newCityId != 0)
        {
            debugSpeakMsg(self, "Entering city " + cityGetName(newCityId));
        }
        return SCRIPT_CONTINUE;
    }
    public void registerCloningFacilities() throws InterruptedException
    {
        int[] cityIds = getAllCityIds();
        for (int i = 0; i < cityIds.length; ++i)
        {
            int cityId = cityIds[i];
            location cityLoc = cityGetLocation(cityId);
            location cloneLoc = cityGetCloneLoc(cityId);
            location cloneRespawn = cityGetCloneRespawn(cityId);
            obj_id cloneId = cityGetCloneId(cityId);
            if (isIdValid(cloneId))
            {
                obj_id planet = getPlanetByName(cityLoc.area);
                if (isIdValid(planet))
                {
                    dictionary params = new dictionary();
                    params.put("id", cloneId);
                    params.put("loc", cloneLoc);
                    params.put("respawn", cloneRespawn);
                    messageTo(planet, "registerCloningFacility", params, 1.0f, false);
                }
            }
        }
    }
    public void dumpInfo(obj_id self, int cityId) throws InterruptedException
    {
        debugSpeakMsg(self, "City name=" + cityGetName(cityId) + " hall=" + cityGetCityHall(cityId) + " loc=" + cityGetLocation(cityId) + " radius=" + cityGetRadius(cityId) + " leader=" + cityGetLeader(cityId) + " incomeTax=" + cityGetIncomeTax(cityId) + " propertyTax=" + cityGetPropertyTax(cityId) + " salesTax=" + cityGetSalesTax(cityId) + " cloneLoc=" + cityGetCloneLoc(cityId) + " cloneRespawn=" + cityGetCloneRespawn(cityId) + " cloneId=" + cityGetCloneId(cityId));
    }
    public int OnHearSpeech(obj_id self, obj_id speaker, String text) throws InterruptedException
    {
        if (speaker != self)
        {
            return SCRIPT_CONTINUE;
        }
        if (text.startsWith("city "))
        {
            java.util.StringTokenizer st = new java.util.StringTokenizer(text);
            st.nextToken();
            int cityId = getCityAtLocation(getLocation(speaker), 0);
            String tok = st.nextToken();
            if (tok.equals("create"))
            {
                createCity(st.nextToken(), speaker, getLocation(speaker), 128, speaker, 1, 2, 3, new location(), 0, false, new location(), new location(), obj_id.NULL_ID);
            }
            else if (tok.equals("destroy"))
            {
                removeCity(cityId);
            }
            else if (tok.equals("info"))
            {
                dumpInfo(speaker, getCityAtLocation(getLocation(speaker), 0));
            }
            else if (tok.equals("militia"))
            {
                citySetCitizenInfo(cityId, speaker, getName(speaker), speaker, 1);
            }
            else if (tok.equals("citizen"))
            {
                citySetCitizenInfo(cityId, speaker, getName(speaker), speaker, 0);
            }
            else if (tok.equals("register"))
            {
                registerCloningFacilities();
            }
        }
        return SCRIPT_CONTINUE;
    }
}
