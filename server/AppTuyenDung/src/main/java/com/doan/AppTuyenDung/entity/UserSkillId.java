package com.doan.AppTuyenDung.entity;
import java.io.Serializable;
import java.util.Objects;

public class UserSkillId implements Serializable {
    private Integer userId;
    private Integer skillId;

    public UserSkillId() {}

    public UserSkillId(Integer userId, Integer skillId) {
        this.userId = userId;
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSkillId that = (UserSkillId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(skillId, that.skillId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, skillId);
    }
}