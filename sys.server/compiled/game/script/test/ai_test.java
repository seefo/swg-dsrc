package script.test;

import script.library.create;
import script.library.healing;
import script.library.utils;
import script.location;
import script.obj_id;

public class ai_test extends script.base_script
{
    public ai_test()
    {
    }
    public void end(obj_id self) throws InterruptedException
    {
        if (hasObjVar(self, "testPet"))
        {
            obj_id mob = getObjIdObjVar(self, "testPet");
            destroyObject(mob);
            removeObjVar(self, "testPet");
        }
    }
    public void start(obj_id self) throws InterruptedException
    {
        if (hasObjVar(self, "testPet"))
        {
            end(self);
        }
        debugSpeakMsg(self, "--Noodle has begun");
        createTriggerVolume("blah", 3, true);
        String creatureType = getStringObjVar(self, "testPetType");
        location blah = getLocation(self);
        blah.x = blah.x + 1.25f;
        obj_id newmob = create.object(creatureType, blah);
        if (newmob == null)
        {
            debugSpeakMsg(self, "--Could not spawn pet");
            return;
        }
        detachAllScripts(newmob);
        attachScript(newmob, "test.report_behavior");
        attachScript(newmob, "test.ai_test2");
        setObjVar(self, "testPet", newmob);
        location loc = getLocation(self);
        setHomeLocation(newmob, loc);
        stop(newmob);
        stopFloating(newmob);
    }
    public void gimme_skills(obj_id speaker) throws InterruptedException
    {
        grantSkill(speaker, "outdoors_creaturehandler_novice");
        grantSkill(speaker, "outdoors_creaturehandler_taming_01");
        grantSkill(speaker, "outdoors_creaturehandler_taming_02");
        grantSkill(speaker, "outdoors_creaturehandler_taming_03");
        grantSkill(speaker, "outdoors_creaturehandler_taming_04");
        grantSkill(speaker, "outdoors_creaturehandler_training_01");
        grantSkill(speaker, "outdoors_creaturehandler_training_02");
        grantSkill(speaker, "outdoors_creaturehandler_training_03");
        grantSkill(speaker, "outdoors_creaturehandler_training_04");
        grantSkill(speaker, "outdoors_creaturehandler_healing_01");
        grantSkill(speaker, "outdoors_creaturehandler_healing_02");
        grantSkill(speaker, "outdoors_creaturehandler_healing_03");
        grantSkill(speaker, "outdoors_creaturehandler_healing_04");
        grantSkill(speaker, "outdoors_creaturehandler_support_01");
        grantSkill(speaker, "outdoors_creaturehandler_support_02");
        grantSkill(speaker, "outdoors_creaturehandler_support_03");
        grantSkill(speaker, "outdoors_creaturehandler_support_04");
        grantSkill(speaker, "outdoors_creaturehandler_master");
        setObjVar(speaker, "fasttame", 1);
    }
    public void start2(obj_id self) throws InterruptedException
    {
        if (hasObjVar(self, "testPet"))
        {
            end(self);
        }
        debugSpeakMsg(self, "--Testing has begun");
        String creatureType = getStringObjVar(self, "testPetType");
        location blah = getLocation(self);
        obj_id newmob = create.object(creatureType, blah);
        if (newmob == null)
        {
            debugSpeakMsg(self, "--Could not spawn pet");
            return;
        }
        location loc = getLocation(self);
        setHomeLocation(newmob, loc);
    }
    public void startMounts(obj_id self, obj_id speaker) throws InterruptedException
    {
        if (hasObjVar(self, "testPet"))
        {
            end(self);
        }
        debugSpeakMsg(self, "--Testing has begun");
        String creatureType = getStringObjVar(self, "testPetType");
        location blah = getLocation(self);
        obj_id newmob = create.object(creatureType, blah);
        if (newmob == null)
        {
            debugSpeakMsg(self, "--Could not spawn pet");
            return;
        }
        location loc = getLocation(self);
        setHomeLocation(newmob, loc);
        attachScript(newmob, "ai.pet_advance");
        gimme_skills(speaker);
    }
    public int OnTriggerVolumeEntered(obj_id self, String volumeName, obj_id whoTriggeredMe) throws InterruptedException
    {
        debugSpeakMsg(self, "Trigger Volume Enter");
        return SCRIPT_CONTINUE;
    }
    public int OnTriggerVolumeExited(obj_id self, String volumeName, obj_id whoTriggeredMe) throws InterruptedException
    {
        debugSpeakMsg(self, "Trigger Volume Exit");
        return SCRIPT_CONTINUE;
    }
    public int OnObjectDamaged(obj_id self, obj_id attacker, obj_id weapon, int damage) throws InterruptedException
    {
        healing.fullHeal(self);
        return SCRIPT_CONTINUE;
    }
    public int OnCreatureDamaged(obj_id self, obj_id attacker, obj_id weapon, int[] damage) throws InterruptedException
    {
        healing.fullHeal(self);
        return SCRIPT_CONTINUE;
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        if(!isGod(self) || getGodLevel(self) < 10 || !isPlayer(self)){
            detachScript(self, "test.ai_test");
        }
        setObjVar(self, "blahblah", 1);
        start(self);
        return SCRIPT_CONTINUE;
    }
    public int OnDetach(obj_id self) throws InterruptedException
    {
        end(self);
        return SCRIPT_CONTINUE;
    }
    public int OnHearSpeech(obj_id self, obj_id speaker, String text) throws InterruptedException
    {
        String[] words = split(text, ' ');
        if (words[0].equals("cone"))
        {
            debugSpeakMsg(self, "happy cone cone");
            float fltConeLength = 64f;
            float fltConeWidth = 15f;
            obj_id objTarget = getLookAtTarget(self);
            obj_id objPlayer = self;
            obj_id[] objDefenders = getCreaturesInCone(objPlayer, objTarget, fltConeLength, fltConeWidth);
            for (int intI = 0; intI < objDefenders.length; intI++)
            {
                debugSpeakMsg(objDefenders[intI], "I am targetsd");
            }
            objDefenders = pvpGetTargetsInCone(objPlayer, objPlayer, objTarget, fltConeLength, fltConeWidth);
            for (int intI = 0; intI < objDefenders.length; intI++)
            {
                debugSpeakMsg(objDefenders[intI], "I am PVP targetsd");
            }
        }
        if (speaker == self)
        {
            return SCRIPT_CONTINUE;
        }
        if (words[0].equals("spawn"))
        {
            String toCreate = words[1];
            location here = getLocation(self);
            obj_id newMob = create.object(toCreate, here);
            stopFloating(newMob);
            debugSpeakMsg(newMob, "i am a spatula");
            return SCRIPT_CONTINUE;
        }
        if (words[0].equals("start"))
        {
            start(self);
            return SCRIPT_CONTINUE;
        }
        else if (words[0].equals("start2"))
        {
            start2(self);
            return SCRIPT_CONTINUE;
        }
        else if (words[0].equals("startMounts"))
        {
            startMounts(self, speaker);
            return SCRIPT_CONTINUE;
        }
        else if (words[0].equals("setPetType"))
        {
            setObjVar(self, "testPetType", words[1]);
            return SCRIPT_CONTINUE;
        }
        else if (words[0].equals("spawnCreatures"))
        {
            String creatureType = getStringObjVar(self, "testPetType");
            for (int i = 0; i < 10; i++)
            {
                location loc = getLocation(self);
                create.object(creatureType, loc);
            }
        }
        else if (words[0].equals("spawnRandom300"))
        {
            String creatureType = getStringObjVar(self, "testPetType");
            for (int i = 0; i < 300; i++)
            {
                location loc = getLocation(self);
                obj_id mob = create.object(creatureType, loc);
                loiterTarget(mob, self, 10, 30, 1, 2);
            }
        }
        else if (words[0].equals("reload"))
        {
            debugSpeakMsg(self, "Reloading node " + words[1]);
            obj_id idToReload = utils.stringToObjId(words[1]);
            obj_id[] ids = new obj_id[1];
            ids[0] = idToReload;
            reloadPathNodes(ids);
        }
        else if (words[0].equals("testSwarm"))
        {
            String creatureType = getStringObjVar(self, "testPetType");
            for (int i = 0; i < 1; i++)
            {
                location loc = getLocation(self);
                obj_id mob = create.object(creatureType, loc);
                swarm(mob, speaker);
                setMovementRun(mob);
            }
        }
        else if (words[0].equals("testSwarm2"))
        {
            String creatureType = getStringObjVar(self, "testPetType");
            for (int i = 0; i < 1; i++)
            {
                location loc = getLocation(self);
                obj_id mob = create.object(creatureType, loc);
                swarm(mob, speaker, 8.0f);
                setMovementRun(mob);
            }
        }
        else if (words[0].equals("testSwarm3"))
        {
            String creatureType = getStringObjVar(self, "testPetType");
            for (int i = 0; i < 1; i++)
            {
                location loc = getLocation(self);
                obj_id mob = create.object(creatureType, loc);
                swarm(mob, speaker, 16.0f);
                setMovementRun(mob);
            }
        }
        if (!hasObjVar(self, "testPet"))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id mob = getObjIdObjVar(self, "testPet");
        if (words[0].equals("follow"))
        {
            debugSpeakMsg(self, "--Now following");
            follow(mob, speaker, 2f, 5f);
        }
        else if (words[0].equals("followOffset"))
        {
            debugSpeakMsg(self, "--Now following");
            follow(mob, speaker, new location(2, 0, 0, ""));
        }
        else if (words[0].equals("swarm"))
        {
            debugSpeakMsg(self, "--Swarm");
            swarm(mob, speaker);
        }
        else if (words[0].equals("swarmit"))
        {
            obj_id target = utils.stringToObjId(words[1]);
            debugSpeakMsg(self, "--Swarming target");
            swarm(mob, target);
        }
        else if (words[0].equals("wander"))
        {
            debugSpeakMsg(self, "--Wandering");
            wander(mob);
        }
        else if (words[0].equals("loiter"))
        {
            debugSpeakMsg(self, "--Loitering");
            final location home = getLocation(mob);
            loiterLocation(mob, home, 15f, 20f, 1f, 2f);
        }
        else if (words[0].equals("loiterNear"))
        {
            debugSpeakMsg(self, "--Loitering");
            loiterTarget(mob, self, 0, 5, 0, 0);
        }
        else if (words[0].equals("stop"))
        {
            debugSpeakMsg(self, "--Stop");
            stop(mob);
        }
        else if (words[0].equals("pathHome"))
        {
            debugSpeakMsg(self, "--Going home");
            location loc = getLocation(self);
            pathTo(mob, loc);
        }
        else if (words[0].equals("pathAway"))
        {
            debugSpeakMsg(self, "--Going to speaker");
            location loc = getLocation(speaker);
            pathTo(mob, loc);
        }
        else if (words[0].equals("moveX"))
        {
            debugSpeakMsg(self, "--Going to speaker");
            location loc = getLocation(mob);
            loc.x += 1;
            pathTo(mob, loc);
        }
        else if (words[0].equals("flee"))
        {
            debugSpeakMsg(self, "--Fleeing");
            flee(mob, speaker, 5, 10);
        }
        else if (words[0].equals("face"))
        {
            debugSpeakMsg(self, "--Facing target");
            faceToBehavior(mob, speaker);
        }
        else if (words[0].equals("faceBehavior"))
        {
            debugSpeakMsg(self, "--Facing behavior");
            faceToBehavior(mob, speaker);
        }
        else if (words[0].equals("end"))
        {
            debugSpeakMsg(self, "--Done with testing");
            end(self);
        }
        else if (words[0].equals("anger"))
        {
            debugSpeakMsg(self, "--Angry at speaker");
            addToMentalStateToward(mob, speaker, ANGER, 40);
        }
        else if (words[0].equals("frenzy"))
        {
            debugSpeakMsg(self, "--Frenzying toward speaker");
            addToMentalStateToward(mob, speaker, ANGER, 100);
        }
        else if (words[0].equals("attack"))
        {
            debugSpeakMsg(self, "--Attacking speaker");
            addToMentalStateToward(mob, speaker, ANGER, 100, BEHAVIOR_ATTACK);
        }
        else if (words[0].equals("attack"))
        {
            debugSpeakMsg(self, "--Attacking speaker");
            addToMentalStateToward(mob, speaker, ANGER, 100, BEHAVIOR_ATTACK);
        }
        else if (words[0].equals("upright"))
        {
            debugSpeakMsg(self, "--setPosture(self, POSTURE_UPRIGHT)");
            setPosture(mob, POSTURE_UPRIGHT);
        }
        else if (words[0].equals("crouch"))
        {
            debugSpeakMsg(self, "--setPosture(self, POSTURE_CROUCHED)");
            setPosture(mob, POSTURE_CROUCHED);
        }
        else if (words[0].equals("prone"))
        {
            debugSpeakMsg(self, "--setPosture(self, POSTURE_PRONE)");
            setPosture(mob, POSTURE_PRONE);
        }
        else if (words[0].equals("lie"))
        {
            debugSpeakMsg(self, "--setPosture(self, POSTURE_LYING_DOWN)");
            setPosture(mob, POSTURE_LYING_DOWN);
        }
        else if (words[0].equals("drive"))
        {
            debugSpeakMsg(self, "--setPosture(self, POSTURE_DRIVING_VEHICLE)");
            setPosture(mob, POSTURE_DRIVING_VEHICLE);
        }
        else if (words[0].equals("stand"))
        {
            debugSpeakMsg(self, "--setLocomotion(self, LOCOMOTION_STANDING");
            setLocomotion(mob, LOCOMOTION_STANDING);
        }
        else if (words[0].equals("walk"))
        {
            debugSpeakMsg(self, "--setLocomotion(self, LOCOMOTION_WALKING");
            setLocomotion(mob, LOCOMOTION_WALKING);
        }
        else if (words[0].equals("run"))
        {
            debugSpeakMsg(self, "--setLocomotion(self, LOCOMOTION_RUNNING");
            setLocomotion(mob, LOCOMOTION_RUNNING);
        }
        else if (words[0].equals("fast"))
        {
            debugSpeakMsg(self, "--setMovementRun(self)");
            setMovementRun(mob);
        }
        else if (words[0].equals("slow"))
        {
            debugSpeakMsg(self, "--setMovementWalk(self)");
            setMovementWalk(mob);
        }
        else if (words[0].equals("pathToName"))
        {
            debugSpeakMsg(self, "--Going to waypoint " + words[1]);
            pathTo(mob, words[1]);
        }
        else if (words[0].equals("canSee"))
        {
            if (canSee(mob, speaker))
            {
                debugSpeakMsg(mob, "Can see you");
            }
            else 
            {
                debugSpeakMsg(mob, "Can NOT see you");
            }
        }
        return SCRIPT_CONTINUE;
    }
}
