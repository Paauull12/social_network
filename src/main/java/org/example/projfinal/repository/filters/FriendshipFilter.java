package org.example.projfinal.repository.filters;

import org.example.projfinal.domain.Friendship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendshipFilter extends Filter {

    public FriendshipFilter(Map<String, String> val) {
        super(val);
    }

    @Override
    public String getFiltersSql() {
        List<String> conditions = new ArrayList<>();

        if (val.get("user1") != null && !val.get("user1").isEmpty()) {
            conditions.add("user1 = ?");
        }

        if (val.get("user2") != null && !val.get("user2").isEmpty()) {
            conditions.add("user2 = ?");
        }

        if (val.get("status") != null && !val.get("status").isEmpty()) {
            conditions.add("status = ?");
        }

        if (!conditions.isEmpty()) {
            return " where " + String.join(" AND ", conditions);
        }

        return "";
    }
}
