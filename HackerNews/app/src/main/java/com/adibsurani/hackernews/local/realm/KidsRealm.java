package com.adibsurani.hackernews.local.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class KidsRealm extends RealmObject {

    @PrimaryKey
    private int kid;

    public int getKid() {
        return kid;
    }

    public void setKid(int kid) {
        this.kid = kid;
    }
}
