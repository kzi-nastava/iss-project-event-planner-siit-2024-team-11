package org.example.eventy.users.dtos;

import org.example.eventy.users.validation.annotation.ValidBlockUser;

@ValidBlockUser
public class BlockUserDTO {
    private Long blockedId;
    private Long blockerId;

    public BlockUserDTO() {}

    public BlockUserDTO(Long blockedId, Long blockerId) {
        this.blockedId = blockedId;
        this.blockerId = blockerId;
    }

    public Long getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(Long blockedId) {
        this.blockedId = blockedId;
    }

    public Long getBlockerId() {
        return blockerId;
    }

    public void setBlockerId(Long blockerId) {
        this.blockerId = blockerId;
    }
}
