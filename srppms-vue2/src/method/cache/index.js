const sessionCache = {
    set(key, value) {
        if (!sessionStorage) {
            return
        }
        if (key != null && value != null) {
            sessionStorage.setItem(key, value)
        }
    },
    get(key) {
        if (!sessionStorage) {
            return null
        }
        if (key == null) {
            return null
        }
        return sessionStorage.getItem(key)
    },
    remove(key) {
        sessionStorage.removeItem(key);
    },
    removeAll() {
        sessionStorage.clear();
    }
}

const localCache = {
    set(key, value) {
        if (!localStorage) {
            return
        }
        if (key != null && value != null) {
            localStorage.setItem(key, value)
        }
    },
    get(key) {
        if (!localStorage) {
            return null
        }
        if (key == null) {
            return null
        }
        return localStorage.getItem(key)
    },
    remove(key) {
        localStorage.removeItem(key);
    },
    removeAll() {
        localStorage.clear();
    }
}

export default {
    /**
     * 会话级缓存
     */
    session: sessionCache,
    /**
     * 本地缓存
     */
    local: localCache
}
