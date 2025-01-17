package script.theme_park.dungeon.mustafar_trials.volcano_battlefield;

import script.dictionary;
import script.library.trial;
import script.obj_id;

public class event_four_guard extends script.base_script
{
    public event_four_guard()
    {
    }
    public static final boolean LOGGING = false;
    public int OnAttach(obj_id self) throws InterruptedException
    {
        beginAttack(self);
        trial.setHp(self, trial.HP_VOLCANO_FOUR_GUARD);
        return SCRIPT_CONTINUE;
    }
    public int OnIncapacitated(obj_id self, obj_id killer) throws InterruptedException
    {
        messageTo(self, "nukeSelf", null, 5, false);
        playClientEffectObj(self, trial.PRT_KUBAZA_WARNING, self, "");
        return SCRIPT_CONTINUE;
    }
    public int nukeSelf(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id[] targets = trial.getValidTargetsInRadius(self, 7.0f);
        playClientEffectLoc(self, trial.PRT_KUBAZA_EXPLODE, getLocation(self), 0.4f);
        destroyObject(self);
        if (targets == null || targets.length == 0)
        {
            doLogging("nukeSelf", "No valid targets in blast radius");
            return SCRIPT_CONTINUE;
        }
        for (int i = 0; i < targets.length; i++)
        {
            damage(targets[i], DAMAGE_ELEMENTAL_HEAT, HIT_LOCATION_BODY, 2000);
        }
        return SCRIPT_CONTINUE;
    }
    public void beginAttack(obj_id self) throws InterruptedException
    {
        setMovementRun(self);
        obj_id[] players = trial.getValidTargetsInRadius(self, 100.0f);
        if (players == null || players.length == 0)
        {
            doLogging("beginAttack", "Found no players to attack");
            return;
        }
        obj_id toAttack = trial.getClosest(self, players);
        if (!isIdValid(toAttack))
        {
            doLogging("beginAttack", "player toAttack, was invalid");
            return;
        }
        startCombat(self, toAttack);
        return;
    }
    public void doLogging(String section, String message) throws InterruptedException
    {
        if (LOGGING || trial.VOLCANO_LOGGING)
        {
            LOG("logging/event_four_guard/" + section, message);
        }
    }
}
