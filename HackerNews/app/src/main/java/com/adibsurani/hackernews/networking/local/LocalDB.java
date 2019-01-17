package com.adibsurani.hackernews.networking.local;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.adibsurani.hackernews.helper.Constants;
import com.adibsurani.hackernews.networking.data.Story;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("CommitPrefEdits")
public class LocalDB extends Activity {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public LocalDB(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(Constants.PREF_NAME, PRIVATE_MODE);
        editor = sharedPref.edit();
    }

    // TOP
    public void setTopStory(ArrayList<Story> topStories) {
        Gson gson = new Gson();
        String story = gson.toJson(topStories);
        editor
                .putString(Constants.TOP, story)
                .commit();
    }

    public List<Story> getTopStory() {
        List<Story> storyList;
        if (sharedPref.contains(Constants.TOP)) {
            String topStory = sharedPref.getString(Constants.TOP, null);
            Gson gson = new Gson();
            Story[] stories = gson.fromJson(topStory, Story[].class);
            storyList = Arrays.asList(stories);
            storyList = new ArrayList<>(storyList);
        } else {
            return null;
        }
        return storyList;
    }

    public void clearTopStory() {
        editor
                .putString(Constants.TOP, null)
                .commit();
    }

    // BEST
    public void setBestStory(ArrayList<Story> bestStories) {
        Gson gson = new Gson();
        String story = gson.toJson(bestStories);
        editor
                .putString(Constants.BEST, story)
                .commit();
    }

    public List<Story> getBestStory() {
        List<Story> storyList;
        if (sharedPref.contains(Constants.BEST)) {
            String bestStory = sharedPref.getString(Constants.BEST, null);
            Gson gson = new Gson();
            Story[] stories = gson.fromJson(bestStory, Story[].class);
            storyList = Arrays.asList(stories);
            storyList = new ArrayList<>(storyList);
        } else {
            return null;
        }
        return storyList;
    }

    public void clearBestStory() {
        editor
                .putString(Constants.BEST, null)
                .commit();
    }

    // NEW
    public void setNewStory(ArrayList<Story> newStories) {
        Gson gson = new Gson();
        String story = gson.toJson(newStories);
        editor
                .putString(Constants.NEW, story)
                .commit();
    }

    public List<Story> getNewStory() {
        List<Story> storyList;
        if (sharedPref.contains(Constants.NEW)) {
            String topStory = sharedPref.getString(Constants.NEW, null);
            Gson gson = new Gson();
            Story[] stories = gson.fromJson(topStory, Story[].class);
            storyList = Arrays.asList(stories);
            storyList = new ArrayList<>(storyList);
        } else {
            return null;
        }
        return storyList;
    }

    public void clearNewStory() {
        editor
                .putString(Constants.NEW, null)
                .commit();
    }

    // BOOKMARK
    public void setBookmark(ArrayList<Story> bookmarks) {
        Gson gson = new Gson();
        String story = gson.toJson(bookmarks);
        editor
                .putString(Constants.BOOKMARK, story)
                .commit();
    }

    public void addBookmark(Story bookmark) {
        List<Story> storyList;
        if (sharedPref.contains(Constants.BOOKMARK)) {
            String story = sharedPref.getString(Constants.BOOKMARK, null);
            Gson gson = new Gson();
            Story[] bookmarks = gson.fromJson(story, Story[].class);
            storyList = Arrays.asList(bookmarks);
            storyList = new ArrayList<>(storyList);
            storyList.add(0,bookmark);
        } else {
            storyList = new ArrayList<>();
            storyList.add(bookmark);
        }
        Gson gson = new Gson();
        String jsonItem = gson.toJson(storyList);
        editor.putString(Constants.BOOKMARK, jsonItem);
        editor.commit();
    }

    public List<Story> getBookmark() {
        List<Story> storyList;
        if (sharedPref.contains(Constants.NEW)) {
            String topStory = sharedPref.getString(Constants.NEW, null);
            Gson gson = new Gson();
            Story[] stories = gson.fromJson(topStory, Story[].class);
            storyList = Arrays.asList(stories);
            storyList = new ArrayList<>(storyList);
        } else {
            return null;
        }
        return storyList;
    }

    public void clearBookMark() {
        editor
                .putString(Constants.BOOKMARK, null)
                .commit();
    }
}
