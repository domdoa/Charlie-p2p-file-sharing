package com.filesharing.iot.repository;

import com.filesharing.iot.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>{
    Group findByName(final String groupName);
    Group findByInviteString(final String inviteString);

}