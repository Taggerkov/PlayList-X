package com.playlistx.model.login;

import java.io.Serializable;

public class HashKey implements Serializable {
    private final UserName userName;
    private final int passwordHash;

    public HashKey(UserName userName, int passwordHash) {
        this.userName = userName;
        this.passwordHash = passwordHash;
    }

    public UserName getUserName() {
        return userName;
    }

    public int getHashPass() {
        return passwordHash;
    }

    public boolean equals(String userName) {
        return this.userName.equals(userName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashKey hashKey)) return false;
        if (!userName.equals(hashKey.userName)) return false;
        return passwordHash == hashKey.passwordHash;
    }
}
