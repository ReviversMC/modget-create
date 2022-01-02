package com.github.reviversmc.modget.create.apicalls;

import com.github.reviversmc.modget.create.data.ModrinthV1ModPojo;
import com.github.reviversmc.modget.create.data.ModrinthV1TeamMemberPojo;
import com.github.reviversmc.modget.create.data.ModrinthV1UserPojo;

public interface ModrinthQuery {

    /**
     * Gets the mod, if it exists.
     * @return Either a filled or empty {@link ModrinthV1ModPojo}, depending on whether the mod exists.
     */
    ModrinthV1ModPojo getMod();

    /**
     * Gets team members of the team which created the mod specified in this class.
     * @return Either a filled or empty array of {@link ModrinthV1TeamMemberPojo}, depending on whether the team exists.
     */
    ModrinthV1TeamMemberPojo[] getTeamMembers();

    /**
     * Gets the owner of this mod.
     * @return Either a filled or empty {@link ModrinthV1UserPojo}, depending on whether the user exists.
     */
    ModrinthV1UserPojo getOwner();

    /**
     * Get if the mod exists.
     * @return True if it exists, false otherwise.
     */
    boolean modExists();

}
