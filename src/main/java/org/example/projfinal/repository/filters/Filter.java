package org.example.projfinal.repository.filters;

import org.example.projfinal.domain.Entity;
import org.example.projfinal.domain.Friendship;

import java.util.Map;

public abstract class Filter {
    
    protected Map<String, String> val;

    public Filter(Map<String, String> val) {
        this.val = val;
    }

    public abstract String getFiltersSql();
}
