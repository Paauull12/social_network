package org.example.projfinal.repository.filters;

import org.example.projfinal.domain.Friendship;
import org.example.projfinal.domain.User;

import java.util.Map;

public class UserFilter extends Filter{

    public UserFilter(Map<String, String> val) {
        super(val);
    }

    @Override
    public String getFiltersSql() {
        if(val.get("username") == null || val.get("username").isEmpty())
            return "";

        return " where username like '" + val.get("username") + "%'";
    }
}
