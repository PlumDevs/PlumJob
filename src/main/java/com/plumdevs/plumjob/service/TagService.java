package com.plumdevs.plumjob.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    @Autowired
    private JdbcTemplate jdbc;

    public Integer getTagIdByName(String tagName) {
        try {
            return jdbc.queryForObject(
                    "SELECT tag_id FROM TagCodes WHERE tag_name = ?",
                    Integer.class,
                    tagName
            );
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public void assignTagToUser(String username, String tagName) {
        String tagType = getTagType(tagName);
        if (tagType == null) return;

        jdbc.update("""
            DELETE FROM TagUsers
            WHERE user_id = ?
              AND tag_id IN (
                  SELECT tag_id FROM TagCodes WHERE tag_name LIKE ?
              )
        """, username, tagType + ":%");

        Integer tagId = getTagIdByName(tagName);
        if (tagId != null) {
            jdbc.update("INSERT INTO TagUsers (user_id, tag_id) VALUES (?, ?)", username, tagId);
        }
    }

    private String getTagType(String tagName) {
        if (tagName == null || !tagName.contains(":")) return null;
        return tagName.split(":")[0];
    }

    public String getTagValueForType(String username, String tagType) {
        try {
            return jdbc.queryForObject("""
            SELECT SUBSTRING_INDEX(tc.tag_name, ':', -1) as tag_value
            FROM TagUsers tu
            JOIN TagCodes tc ON tu.tag_id = tc.tag_id
            WHERE tu.user_id = ? AND tc.tag_name LIKE ?
            LIMIT 1
        """, String.class, username, tagType + ":%");
        } catch (Exception e) {
            return null;
        }
    }

}
