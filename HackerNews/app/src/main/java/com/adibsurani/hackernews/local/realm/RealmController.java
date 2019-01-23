package com.adibsurani.hackernews.local.realm;

import android.app.Activity;
import android.app.Application;
import androidx.fragment.app.Fragment;
import com.adibsurani.hackernews.networking.data.Story;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {
        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    public void refresh() {
        realm.refresh();
    }

    public void clearAll() {
        realm.beginTransaction();
        realm.clear(StoryRealm.class);
        realm.commitTransaction();
    }

    public RealmResults<StoryRealm> getBookmark() {
        return realm.where(StoryRealm.class).findAll();
    }

    public StoryRealm getBookmarkByID(String id) {
        return realm
                .where(StoryRealm.class)
                .equalTo("id", id)
                .findFirst();
    }

    public boolean hasBookmark() {
        return !realm.allObjects(StoryRealm.class).isEmpty();
    }

    public void addBookmark(Story story) {
        StoryRealm storyObject = new StoryRealm();
        storyObject.setId(story.getId());
        if (story.getAuthor() != null) {
            storyObject.setAuthor(story.getAuthor());
        }
        if (story.getDescendants() != null) {
            storyObject.setDescendants(story.getDescendants());
        }
//        if (story.getKids() != null) {
//            storyObject.setKids(story.getKids());
//        }
        if (story.getTitle() != null) {
            storyObject.setTitle(story.getTitle());
        }
        if (story.getType() != null) {
            storyObject.setType(story.getType());
        }
        if (story.getUrl() != null) {
            storyObject.setUrl(story.getUrl());
        }
        storyObject.setScore(story.getScore());
        storyObject.setTime(story.getTime());
        realm.beginTransaction();
        realm.copyToRealm(storyObject);
        realm.commitTransaction();
    }

    public void removeBookmark(String id) {
        StoryRealm storyObject = realm
                .where(StoryRealm.class)
                .equalTo("id", id)
                .findFirst();
        if (storyObject != null) {
            realm.beginTransaction();
            storyObject.removeFromRealm();
            realm.commitTransaction();
        }
    }
}
