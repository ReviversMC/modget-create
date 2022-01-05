package com.github.reviversmc.modget.create.apicalls;

import com.github.reviversmc.modget.create.data.ModrinthV1ModPojo;
import com.github.reviversmc.modget.create.data.ModrinthV1TeamMemberPojo;
import com.github.reviversmc.modget.create.data.ModrinthV1UserPojo;

import java.util.Optional;

public interface ModrinthQuery {

    /**
     * Gets the mod, if it exists.
     *
     * @return A {@link Optional<ModrinthV1ModPojo>},
     * if the mod exists, or an empty {@link Optional} otherwise.
     */
    Optional<ModrinthV1ModPojo> getMod();

    /**
     * Gets team members of the team which created the mod specified in this class.
     *
     * @return A {@link Optional} <{@link ModrinthV1TeamMemberPojo}[]>,
     * if the mod exists, or an empty {@link Optional} otherwise.
     */
    Optional<ModrinthV1TeamMemberPojo[]> getTeamMembers();

    /**
     * Gets the owner of this mod.
     *
     * @return A {@link Optional<ModrinthV1UserPojo>},
     * if the mod exists, or an empty {@link Optional} otherwise.
     */
    Optional<ModrinthV1UserPojo> getOwner();

    /**
     * Get if the mod exists.
     *
     * @return True if it exists, false otherwise.
     */
    boolean modExists();

}
